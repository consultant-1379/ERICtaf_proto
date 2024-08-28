package com.ericsson.cifwk.taf.run;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.MavenInvocationException;

public abstract class ExecutionHelper extends AbstractMojo {
	@Component
	protected MavenProject project;

	@Component
	protected MavenSession session;

	@Component
	protected BuildPluginManager pluginManager;

	protected String getGroupId(Map<String, String> gavItem) {
		return gavItem.get("groupId");
	}

	protected String getVersion(Map<String, String> gavItem) {
		return gavItem.get("version");
	}

	protected String getArtifactId(Map<String, String> gavItem) {
		return gavItem.get("artifactId");
	}

	protected String getGavString(Map<String, String> gavItem) {
		StringBuilder gavString = new StringBuilder();
		gavString.append(getGroupId(gavItem)).append(":")
				.append(getArtifactId(gavItem)).append(":")
				.append(getVersion(gavItem));
		return gavString.toString();
	}

	/**
	 * Get target folder location
	 * 
	 * @return
	 */
	protected String getTarget() {
		return project.getBuild().getDirectory() + "/";
	}

	/**
	 * Use maven invoker in specified directory
	 * 
	 * @param goal
	 * @param properties
	 * @param baseDir
	 * @return true if exit code is 0
	 */
	protected boolean invoke(String goal, Properties properties, File baseDir) {
		InvocationRequest request = new DefaultInvocationRequest()
				.setBaseDirectory(baseDir)
				.setGoals(Collections.singletonList(goal))
				.setProperties(properties);
		DefaultInvoker invoker = new DefaultInvoker();
		getLog().debug(
				"Invoking " + request + "on " + baseDir + " with " + goal);
		try {
			InvocationResult result = invoker.execute(request);
			int exitCode = result.getExitCode();
			getLog().debug("Execution completed with exit code: " + exitCode );
			getLog().debug("Exception thrown is: " + result.getExecutionException());
			return  exitCode == 0;
		} catch (MavenInvocationException e) {
			getLog().debug(e);
			return false;
		}
	}

	/**
	 * Use maven dependency plugin to fetch dependency from ARM
	 * 
	 * @param artifactToGet
	 * @param dest
	 * @param packaging
	 * @return
	 */
	protected boolean invokeGetDependency(Map<String, String> artifactToGet,
			String dest, String packaging) {
		final String goal = "-U org.apache.maven.plugins:maven-dependency-plugin:2.5.1:get";
		Properties properties = new Properties();
		properties.setProperty("artifact", getGavString(artifactToGet));
		properties.setProperty("dest", dest);
		properties.setProperty("packaging", packaging);
		return invoke(goal, properties, project.getBasedir());
	}

}
