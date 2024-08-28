package com.ericsson.qa;

import static org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 06/10/2016
 */
@Mojo(name = "check",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class TestwareCheckMojo extends AbstractMojo {

    private static final String MAVEN_DEPENDENCY_PLUGIN_VERSION = "2.10";
    private static final String CHECKSTYLE_PLUGIN_VERSION = "2.17";
    private static final String PMD_PLUGIN_VERSION = "3.6";
    private static final String HUNTBUGS_PLUGIN_VERSION = "0.0.11";
    private static final String FINDBUGS_PLUGIN_VERSION = "3.0.3";
    private static final String TAF_FINDBUGS_PLUGIN_VERSION = "RELEASE";
    private static final String TAF_HUNTBUGS_PLUGIN_VERSION = "RELEASE";

    public static final String TAF_QA_PLUGIN = "taf.qa-plugin.";
    public static final String DISABLE_CHECKSTYLE_PROPERTY = TAF_QA_PLUGIN + "disableCheckstyle";
    public static final String DISABLE_PMD_PROPERTY = TAF_QA_PLUGIN + "disablePmd";
    public static final String DISABLE_HUNT_BUGS_PROPERTY = TAF_QA_PLUGIN + "disableHuntBugs";
    public static final String DISABLE_FIND_BUGS_PROPERTY = TAF_QA_PLUGIN + "disableFindBugs";


    @Parameter(defaultValue = "true")
    private boolean runPmd;

    @Parameter(defaultValue = "true")
    private boolean runHuntBugs;

    /**
     * Copy-paste finding threshold after which PMD will fail
     */
    @Parameter(defaultValue = "44")
    private Integer minCopyPasteThreshold;

    @Parameter(defaultValue = "true")
    private boolean runCheckstyle;

    @Parameter(defaultValue = "true")
    private boolean runFindBugs;

    /**
     * Include test sources or not
     */
    @Parameter(defaultValue = "true")
    private boolean includeTests;

    /**
     * Ant-style selectors that MAY be considered by the underlying code check plugins to ignore the mentioned paths
     */
    @Parameter
    private List excludedPaths;

    /**
     * Skip the execution (can be useful in particular child modules of the project)
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    @Component
    private MavenProject project;

    @Component
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    @Component
    private PluginDescriptor pluginDescriptor;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        Log logger = getLog();
        if (skip) {
            logger.info("SKIPPING CODE CHECKS");
            return;
        }

        ExecutionEnvironment pluginEnv = executionEnvironment(project, mavenSession, pluginManager);
        PluginRunner pluginRunner = new PluginRunner(logger, pluginEnv);

        long startTime = System.currentTimeMillis();
        try {
            File tempDirFile = Files.createTempDir();
            tempDirFile.deleteOnExit();
            String tempDir = tempDirFile.toString();

            unpackResources(tempDir, pluginRunner);

            if (runCheckstyle && !isDisabled(DISABLE_CHECKSTYLE_PROPERTY)) {
                executeCheckstyle(tempDir, pluginRunner);
            }
            if (runPmd && !isDisabled(DISABLE_PMD_PROPERTY)) {
                executeBasicPmdChecks(pluginRunner);
                executePmdCopyPasteChecks(pluginRunner);
            }
            if (runFindBugs && !isDisabled(DISABLE_FIND_BUGS_PROPERTY)) {
                executeFindBugs(tempDir, pluginRunner);
            }
            if (runHuntBugs && !isDisabled(DISABLE_HUNT_BUGS_PROPERTY)) {
                if (jdkVersion() >= 8) {
                    executeHuntBugs(pluginRunner);
                } else {
                    logger.info(String.format("HuntBugs is Disabled. Java version is %d. Required Java version >= 1.8", jdkVersion()));
                }
            }
        } finally {
            logger.info(String.format("Code check routine took %d millis", System.currentTimeMillis() - startTime));
        }
    }

    private String getIntegrationPomPmdRuleset() {
        try {
            Plugin plugin = project.getParent().getPlugin("org.apache.maven.plugins:maven-pmd-plugin");

            Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
            Xpp3Dom rulesets = configuration.getChild("rulesets");
            return rulesets.getChild("ruleset").getValue();
        } catch (Exception e) {
            getLog().error("FAILED TO RETRIEVE INTEGRATION POM PMD RULESET");
            return "";
        }
    }

    private boolean isDisabled(String disableXProperty) {
        String property = System.getProperty(disableXProperty);
        getLog().info("[PROPERTY] " + disableXProperty + " = " + property);
        return property != null && Boolean.parseBoolean(property);
    }

    private int jdkVersion() {
        try {
            String javaVersion = System.getProperty("java.version");
            String version = javaVersion.split("\\.")[1];
            return Integer.parseInt(version);
        } catch (Exception e) {
            getLog().warn("Error identifying Java Version", e);
        }
        return 0;
    }

    private void unpackResources(String rulesDir, PluginRunner pluginRunner) throws MojoExecutionException {
        Xpp3Dom cfg = configuration(
                element(name("artifactItems"),
                        element(name("artifactItem"),
                                element(name("groupId"), pluginDescriptor.getGroupId()),
                                element(name("artifactId"), "testware-quality-check-resources"),
                                // Resources have the same version as the plugin itself
                                element(name("version"), pluginDescriptor.getVersion()),
                                element(name("type"), "jar"),
                                element(name("includes"), "**/*.xml, **/*.properties")
                        )),
                element(name("outputDirectory"), rulesDir),
                element(name("overWriteIfNewer"), "true"),
                element(name("overWriteReleases"), "true"),
                element(name("overWriteSnapshots"), "true")
        );
        Plugin dependencyPlugin = plugin("org.apache.maven.plugins", "maven-dependency-plugin", MAVEN_DEPENDENCY_PLUGIN_VERSION);
        pluginRunner.run(dependencyPlugin, "rule unpacking", cfg, "unpack");
    }

    private void executeCheckstyle(String rulesDir, PluginRunner pluginRunner) throws MojoExecutionException {
        Xpp3Dom cfg = configuration(
                element(name("consoleOutput"), "true"),
                element(name("logViolationsToConsole"), "true"),
                //TODO: check how this property works, is it needed, etc.:
                element(name("configLocation"), "/enm/checkstyle/checkstyle.xml"),
                element(name("suppressionsLocation"), gluePaths(rulesDir, "checkstyle/checkstyle-suppressions.xml")),
                element(name("suppressionsFileExpression"), gluePaths(rulesDir, "checkstyle/checkstyle.suppressions.file")),
                element(name("violationSeverity"), "info"),
                element(name("includeTestSourceDirectory"), String.valueOf(includeTests)),
                element(name("propertiesLocation"), gluePaths(rulesDir, "checkstyle/checkstyle.properties")),
                element(name("failOnViolation"), "true"),
                element(name("maxAllowedViolations"), "1")
        );
        List<Dependency> dependencies = getCheckstyleDependencies();
        Plugin plugin = plugin("org.apache.maven.plugins", "maven-checkstyle-plugin", CHECKSTYLE_PLUGIN_VERSION, dependencies);
        pluginRunner.run(plugin, "Checkstyle checks", cfg, "check");
    }

    private String gluePaths(String first, String... more) {
        return Paths.get(first, more).toString();
    }


    private void executeBasicPmdChecks(PluginRunner pluginRunner) throws MojoExecutionException {
        // 1. Analyze
        Xpp3Dom cfg = configuration(
                element(name("includeTests"), String.valueOf(includeTests)),
                element(name("linkXRef"), "false"),
                element(name("rulesets"), element(name("ruleset"), getIntegrationPomPmdRuleset()))
        );
        addPmdExclusionsIfDefined(cfg);
        runPmd("PMD basic checks", pluginRunner, cfg, "pmd");

        // 2. Check results
        runPmd("PMD basic checks verification", pluginRunner, configuration(), "check");
    }

    private void executePmdCopyPasteChecks(PluginRunner pluginRunner) throws MojoExecutionException {
        // 1. Analyze
        Xpp3Dom cfg = configuration(
                element(name("includeTests"), String.valueOf(includeTests)),
                element(name("linkXRef"), "false"),
                element(name("minimumTokens"), String.valueOf(minCopyPasteThreshold))
        );
        addPmdExclusionsIfDefined(cfg);
        runPmd("PMD copy-paste checks", pluginRunner, cfg, "cpd");

        // 2. Check results
        runPmd("PMD copy-paste checks verification", pluginRunner, configuration(), "cpd-check");
    }

    private void runPmd(String activityName, PluginRunner pluginRunner, Xpp3Dom cfg, String... goals)
            throws MojoExecutionException {
        Plugin plugin = plugin("org.apache.maven.plugins", "maven-pmd-plugin", PMD_PLUGIN_VERSION);
        pluginRunner.run(plugin, activityName, cfg, goals);
    }

    private void addPmdExclusionsIfDefined(Xpp3Dom cfg) {
        if (excludedPaths != null) {
            String excluded = Joiner.on(", ").join(excludedPaths);
            if (!excluded.isEmpty()) {
                cfg.addChild(element(name("excludes"), element(name("exclude"), excluded)).toDom());
            }
        }
    }

    private void executeFindBugs(String rulesDir, PluginRunner pluginRunner) throws MojoExecutionException {
        // 1. Analyze
        Xpp3Dom cfg = configuration(
                element(name("includeTests"), String.valueOf(includeTests)),
                element(name("effort"), "Max"),
                element(name("threshold"), "low"),
                element(name("xmlOutput"), "true"),
                element(name("excludeFilterFile"), gluePaths(rulesDir, "findbugs/findbugs-exclude.xml")),
                element(name("failOnError"), "true"),
                element(name("fork"), "true"),
                element(name("plugins"),
                        element(name("plugin"),
                                element(name("groupId"), "com.ericsson.cifwk"),
                                element(name("artifactId"), "taf-findbugs-plugin"),
                                element(name("version"), TAF_FINDBUGS_PLUGIN_VERSION)
                        ))
        );
        Plugin plugin = plugin("org.codehaus.mojo", "findbugs-maven-plugin", FINDBUGS_PLUGIN_VERSION);
        pluginRunner.run(plugin, "FindBugs checks", cfg, "findbugs");

        // 2. Check results
        pluginRunner.run(plugin, "FindBugs checks verification", configuration(), "check");
    }

    private void executeHuntBugs(PluginRunner pluginRunner) throws MojoExecutionException {
        List<Dependency> huntBugsDependencies = getHuntBugsDependencies();
        Plugin plugin = plugin("one.util", "huntbugs-maven-plugin", HUNTBUGS_PLUGIN_VERSION, huntBugsDependencies);
        pluginRunner.run(plugin, "HuntBugs checks", configuration(), "huntbugs");
    }

    private List<Dependency> getCheckstyleDependencies() {
        List<Dependency> dependencies = new ArrayList<>();
        // TODO: Update version till 7.0 once JDK 1.8 is used
        Dependency puppyCrawlTools = dependency("com.puppycrawl.tools", "checkstyle", "6.11.2");
        Dependency ericssonExtension = dependency("com.ericsson.cds", "checkstyle-module", "RELEASE");
        dependencies.add(puppyCrawlTools);
        dependencies.add(ericssonExtension);
        return dependencies;
    }

    private List<Dependency> getHuntBugsDependencies() {
        List<Dependency> dependencies = new ArrayList<>();
        Dependency tafHbPlugin = dependency("com.ericsson.cifwk", "taf-huntbugs-plugin", TAF_HUNTBUGS_PLUGIN_VERSION);
        dependencies.add(tafHbPlugin);
        return dependencies;
    }

    private Dependency dependency(String groupId, String artifactId, String version) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

}
