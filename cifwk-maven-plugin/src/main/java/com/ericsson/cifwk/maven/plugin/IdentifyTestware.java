package com.ericsson.cifwk.maven.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

@Mojo( name="deploy" )
@Execute(goal="deploy")
public class IdentifyTestware extends AbstractMojo{
	
	private final String testProp = "taf_testware";
	private final String testModuleNaming = "ERICTAF";
	private Map<String,String> testPomGAV = new HashMap<String, String>();
	private List<Map<String,String>> testwareProjectGAVList = new ArrayList<Map<String,String>>();
	
	@Component
	private MavenSession session;
	
	@Component
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		getLog().info("Checking "+project.getName()+" for testware");
		
		if(Boolean.valueOf(project.getProperties().getProperty(testProp))==true){
			testPomGAV.put("GroupId", project.getGroupId());
			testPomGAV.put("ArtifactId", project.getArtifactId());
			testPomGAV.put("Version", project.getVersion());
			getLog().info("Retrieving test-pom GAV: GroupId is "+project.getGroupId()+", ArtifactId is "+project.getArtifactId()+", Version is "+project.getVersion());
			
			Set<Artifact> artifacts = project.getDependencyArtifacts();
			for( Artifact artifact:artifacts){
				if(artifact.getArtifactId().contains(testModuleNaming)){
					Map<String,String> moduleGAV = new HashMap<String, String>();
					moduleGAV.put("GroupId", artifact.getGroupId());
					moduleGAV.put("ArtifactId", artifact.getArtifactId());
					moduleGAV.put("Version", artifact.getVersion());
					getLog().info("Adding Module GAV: GroupId is "+artifact.getGroupId()+", ArtifactId is "+artifact.getArtifactId()+", Version is "+artifact.getVersion());
					testwareProjectGAVList.add(moduleGAV);
				}
			}
		}
		
		new DatabaseUpdate(testPomGAV, testwareProjectGAVList, getLog()).update();
	}

}
