package com.ericsson.ci.taf.thomas.example.json.config.impl;

import java.io.Reader;
import java.io.Writer;

import org.apache.commons.configuration.AbstractHierarchicalFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public class JsonConfiguration extends AbstractHierarchicalFileConfiguration {

	public JsonConfiguration(String filename) throws ConfigurationException {
		super(filename);
	}

	public JsonConfiguration() {
	}

	public void load(Reader in) throws ConfigurationException {
		TafJsonParser newParser = new TafJsonParser(in);

		JsonConfiguration newConfig = newParser.parse();

		setRootNode(newConfig.getRootNode());
	}

	public void save(Writer out) throws ConfigurationException {
	}

}
