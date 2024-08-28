package com.ericsson.ci.taf.thomas.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import com.ericsson.ci.taf.thomas.example.json.config.impl.JsonConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdvancedJsonExample {

	public static void main(String[] args) throws ConfigurationException, IOException{
		System.out.println("Create Config Factory");
		CompositeConfiguration config = new CompositeConfiguration();
//		config.addConfiguration(new SystemConfiguration());
//		config.addConfiguration(new PropertiesConfiguration("default_taffit_hosts.properties"));
		config.addConfiguration(new JsonConfiguration("/home/ethomev/Work/gitrepos/ERICtaf_proto/apache-commons-configurations/src/main/resources/json_format.json"));

		
		Iterator<String> keys = config.getKeys();
		
		System.out.println("Start printing properties");
		while(keys.hasNext()){
			String key = keys.next();
			System.out.println("Key is: "+key+"\tValue is: "+config.getProperty(key));
		}
			
		

	}
}
