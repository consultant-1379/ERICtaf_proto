package com.ericsson.cifwk.taf.gitchangelog.maven;

import com.ericsson.cifwk.taf.gitchangelog.api.Generator;
import com.ericsson.cifwk.taf.gitchangelog.renderers.ChangeLogRenderer;
import com.ericsson.cifwk.taf.gitchangelog.utils.Defaults;
import com.ericsson.cifwk.taf.gitchangelog.utils.Formatter;
import com.ericsson.cifwk.taf.gitchangelog.converters.JiraIssueLinkConverter;
import com.ericsson.cifwk.taf.gitchangelog.converters.MessageConverter;
import com.ericsson.cifwk.taf.gitchangelog.renderers.SimpleHtmlRenderer;
import com.ericsson.cifwk.taf.gitchangelog.converters.TAFTagConverter;
import com.ericsson.cifwk.taf.gitchangelog.utils.NoGitRepositoryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Goal which generates a changelog based on commits made to the current git repo.
 * This plugin should only run once against the aggregator pom
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, aggregator = true)
public class GenerateMojo extends AbstractMojo {

    /**
     * The directory to put the reports in. Defaults to the project build directory (normally target).
     */
    @Parameter(property="outputDirectory", defaultValue = "${project.build.directory}")
    protected File outputDirectory;

    /**
     * The title of the reports. Defaults to: ${project.name} v${project.version} changelog
     */
    @Parameter(defaultValue = "${project.name} v${project.version} changelog")
    private String reportTitle;

    /**
     * If true, then a simple HTML changelog will be generated.
     */
    @Parameter(defaultValue = "true")
    private boolean generateSimpleHTMLChangeLog;

    /**
     * The filename of the simple HTML changelog, if generated.
     */
    @Parameter(defaultValue = "changelog.html")
    private String simpleHTMLChangeLogFilename;

    /**
     * If true, the changelog will be printed to the Maven build log during packaging.
     */
    @Parameter(defaultValue = "false")
    private boolean verbose;

    /**
     * Used to create links to your issue tracking system for HTML reports. If unspecified, it will try to use the value
     * specified in the issueManagement section of your project's POM. The following values are supported:
     * a value containing the string "github" for the GitHub Issue tracking software;
     * a value containing the string "jira" for Jira tracking software.
     * Any other value will result in no links being made.
     */
    @Parameter(property = "project.issueManagement.system")
    private String issueManagementSystem;

    /**
     * Used to create links to your issue tracking system for HTML reports. If unspecified, it will try to use the value
     * specified in the issueManagement section of your project's POM.
     */
    @Parameter(property = "project.issueManagement.url")
    private String issueManagementUrl;

    /**
     * Regexp pattern to extract the number from the message.
     * The default is: "Bug (\\d+)".
     * Group 1 (the number) is used in links, so "?:" must be used any non relevant group,
     * ex.:
     * (?:Bug|UPDATE|FIX|ADD|NEW|#) ?#?(\d+)
     */
    @Parameter(defaultValue = "Bug (\\d+)")
    private String bugzillaPattern;

    /**
     * Used to set date format in log messages.
     */
    @Parameter(defaultValue = "yyyy-MM-dd HH:mm:ss Z")
    private String dateFormat;

    /**
     * If true, the changelog will include the full git message rather that the short git message
     */
    @Parameter(defaultValue = "false")
    private boolean fullGitMessage;

    /**
     * Include in the changelog the commits after this parameter value.
     */
    @Parameter(defaultValue = "1970-01-01 00:00:00.0 AM")
    private Date includeCommitsAfter;

    /**
     * <p>Multiline commit message separator to use to split commit messages.
     * <p>Leave blank to always treat commit as a single entity
     */
    @Parameter(defaultValue = "")
    private String multiLineCommitMsgSeparator;

    /**
     * Project name
     */
    @Parameter(property = "changelog.title",defaultValue = "${project.name}")
    private String projectName;

    /**
     * Template name
     */
    @Parameter(defaultValue = "SimpleHtmlTemplate")
    private String templateName;

    /**
     * Optional link to a manually created page with remarkable features introduced in each release
     */
    @Parameter(property = "changelog.rlink")
    private String releaseNotesLink;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("Generating changelog in " + outputDirectory.getAbsolutePath());

        File f = outputDirectory;
        if (!f.exists()) {
            f.mkdirs();
        }
        List<ChangeLogRenderer> renderers;
        try {
            renderers = createRenderers();
        } catch (Exception e) {
            log.warn("Error while setting up gitlog renderers.  No changelog will be generated.", e);
            return;
        }

        Generator generator = new Generator(renderers, Defaults.COMMIT_FILTERS, log);

        try {
            generator.openRepository();
        } catch (IOException e) {
            log.warn("Error opening git repository.  Is this Maven project hosted in a git repository? " + "No changelog will be generated.", e);
            return;
        } catch (NoGitRepositoryException e) {
            log.warn("This maven project does not appear to be in a git repository, " + "therefore no git changelog will be generated.");
            return;
        }

        if (!"".equals(dateFormat)) {
            Formatter.setFormat(dateFormat, log);
        }

        try {
            generator.generate(reportTitle, includeCommitsAfter);
        } catch (IOException e) {
            log.warn("Error while generating changelog.  Some changelogs may be incomplete or corrupt.", e);
        }
    }

    private List<ChangeLogRenderer> createRenderers() {
        ArrayList<ChangeLogRenderer> renderers = new ArrayList<ChangeLogRenderer>();

        MessageConverter messageConverter = getCommitMessageConverter();
        MessageConverter tagConverter = getTagConverter();
        if (generateSimpleHTMLChangeLog) {
            Properties templateProperties = new Properties();
            templateProperties.setProperty("project", projectName);
            if (StringUtils.isNotBlank(releaseNotesLink)) {
                templateProperties.setProperty("releaseNotesLink", releaseNotesLink);
            }
            renderers.add(new SimpleHtmlRenderer(getLog(), outputDirectory, simpleHTMLChangeLogFilename,
                    fullGitMessage, messageConverter, tagConverter, templateName, templateProperties, multiLineCommitMsgSeparator));
        }

        return renderers;
    }

    private MessageConverter getCommitMessageConverter() {
        getLog().debug("Trying to load issue tracking info: " + issueManagementSystem + " / " + issueManagementUrl);
        MessageConverter converter = new JiraIssueLinkConverter(getLog());
        getLog().debug("Using tracker " + converter.getClass().getSimpleName());
        return converter;
    }

    private MessageConverter getTagConverter() {
        getLog().debug("Getting Tag Converter");
        MessageConverter converter = new TAFTagConverter(getLog());
        getLog().debug("Using tracker " + converter.getClass().getSimpleName());
        return converter;
    }

}
