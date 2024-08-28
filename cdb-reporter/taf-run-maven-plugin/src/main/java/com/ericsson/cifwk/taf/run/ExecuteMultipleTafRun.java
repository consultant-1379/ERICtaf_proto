package com.ericsson.cifwk.taf.run;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.ericsson.cifwk.taf.mm.RunTree;
import com.ericsson.cifwk.taf.run.schedule.Schedule;
import com.ericsson.cifwk.taf.run.schedule.ScheduleItem;

/**
 * Goal which executes multiple testware projects
 */
@Mojo(name = "run")
@Execute(goal = "run")
public class ExecuteMultipleTafRun extends ExecutionHelper {

	private static final String scheduleFile = "target/schedule/schedule.jar";
	private static final String CDB_PHASE = "CDB";
	/**
	 * List of the GAVs to test projects
	 */
	@Parameter(alias = "artifactItems", required = true)
	private List<Map> projectGavs;

	@Parameter(alias = "schedule", required = true)
	private List<Map> schedule;

	@Parameter(alias = "phase", property = "phase", defaultValue = "CDB", required = false)
	private String phase;

	private List<File> executedTestWareReports = new ArrayList<File>();

	/**
	 * Run tests based on specified artifact items list, schedule and phase
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		downloadTestware();
		downloadSchedule();
		Schedule run = new Schedule(new File(scheduleFile),
				((Object) schedule.get(1)).toString());

		boolean overallRunResult = true;
		for (ScheduleItem testWareToRun : run.getItems()) {
			overallRunResult &= executeRun(testWareToRun);
		}

		prepareReport(executedTestWareReports);
		if (!overallRunResult)
			throw new MojoFailureException("Test execution failed");
	}

	private void downloadTestware() throws MojoExecutionException {
		for (Map<String, String> artifactItem : projectGavs) {
			if (!getPoms(artifactItem)) {
				throw new MojoExecutionException("Testware artefact "
						+ artifactItem + " is missing");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void downloadSchedule() throws MojoExecutionException {
		if (!getSchedule(schedule.get(0))) {
			throw new MojoExecutionException("Schedule artefact "
					+ schedule.get(0) + " is missing");
		}

	}

	private void prepareReport(List<File> files) {
		String phaseTitle = (phase != null) ? phase : CDB_PHASE;
		RunTree.combine(phaseTitle + " run", "target/Jcat_LOGS",
				files.toArray(new File[files.size()]));
	}

	private boolean executeRun(ScheduleItem testWare)
			throws MojoExecutionException, MojoFailureException {
		String dirToRun = testWare.getComponent();
		String suitesToRun = testWare.getSuite();
		String groups = testWare.getGroups();
		File testWareDir = new File(getTarget() + dirToRun);
		getLog().info(
				"Starting tests on " + dirToRun + " using suites "
						+ suitesToRun);
		boolean result = false;
		if (testWareDir.exists()) {
			injectPlugin(testWareDir.getAbsolutePath());
			result = runTests(testWareDir.getAbsolutePath(), suitesToRun,
					groups);
			getLog().info("Run Tests result is: " + result);
			File runFile = new File(testWareDir.getAbsolutePath() + "/"
					+ RunTree.FILE_NAME);
			getLog().debug("RunF File is: "+ runFile.getAbsolutePath());
			result &= RunTree.evaluate(runFile);
			getLog().info(
					"Tests in " + dirToRun + " finished with result: " + result);
			executedTestWareReports.add(runFile);
		} else if (canSkip()) {
			getLog().info(
					"Skipping execution of " + dirToRun
							+ " as testware is not available");
			return true;
		} else
			throw new MojoExecutionException(
					"Scheduled component "
							+ dirToRun
							+ " cannot be executed as it is not available and cannot be skipped!");
		if (testWare.isStopOnFailure() && !result) {
			prepareReport(executedTestWareReports);
			throw new MojoFailureException("Component " + dirToRun
					+ " failed test execution");
		}
		return result;
	}

	private void injectPlugin(String dirContainingPom) {
		final File pomFile = new File(dirContainingPom + "/pom.xml");
		final Dependency plugin = new Dependency();
		plugin.setGroupId("com.ericsson.cifwk.taf");
		plugin.setArtifactId("cdb-reporter-taf-plugin");
		plugin.setVersion(getCdbReporterVersion());
		getLog().debug(
				"Injecting CDB dependencies into " + pomFile.getAbsolutePath());
		try {
			Model pomModel = new MavenXpp3Reader()
					.read(new FileReader(pomFile));
			pomModel.addDependency(plugin);
			new MavenXpp3Writer().write(new FileWriter(pomFile), pomModel);
		} catch (XmlPullParserException | IOException e) {
			getLog().error("Plugin cannot be injected", e);
		}
	}

	private String getCdbReporterVersion() {
		return ((Plugin) project.getBuild().getPluginsAsMap()
				.get("com.ericsson.cifwk.taf:taf-run-maven-plugin"))
				.getVersion();
	}

	private boolean getPoms(Map<String, String> pomToGet) {
		String dir = getTarget() + getGroupId(pomToGet) + "."
				+ getArtifactId(pomToGet);
		return invokeGetDependency(pomToGet, dir + "/pom.xml", "pom");
	}

	private boolean getSchedule(Map<String, String> scheduleToGet) {
		return invokeGetDependency(scheduleToGet, scheduleFile, "jar");
	}

	private boolean canSkip() {
		return (phase == null || !phase.equalsIgnoreCase(CDB_PHASE));
	}

	private boolean runTests(String directory, String suites, String groups) {
		getLog().info("Starting run in" + directory + " for suites: " + suites);
		String goal = "-U test -Psuites";
		File dir = new File(directory);
		Properties props = new Properties(session.getUserProperties());
		props.putAll(session.getUserProperties());
		if (suites != null && suites.length() > 0) {
			props.setProperty("suites", suites);
		}
		if (groups != null && groups.length() > 0)
			props.setProperty("includegroups", groups);
		return invoke(goal, props, dir);
	}

}
