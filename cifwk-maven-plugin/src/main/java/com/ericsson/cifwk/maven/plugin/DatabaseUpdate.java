package com.ericsson.cifwk.maven.plugin;

import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

public class DatabaseUpdate {
	
	Map<String, String> testPomGAV;
	List<Map<String,String>> testwareProjects;
	Log log;
	
	public DatabaseUpdate(Map<String, String> testPomGAV, List<Map<String,String>> testwareProjects, Log log){
		this.testPomGAV = testPomGAV;
		this.testwareProjects = testwareProjects;
		this.log = log;
	}
	
	public void update(){
		log.info("Recieved test-pom artifact " +testPomGAV.get("ArtifactId"));
		for(Map<String,String> testwareGAV:testwareProjects){
			log.info("And testware artifact " +testwareGAV.get("ArtifactId"));
		}
	}

}
