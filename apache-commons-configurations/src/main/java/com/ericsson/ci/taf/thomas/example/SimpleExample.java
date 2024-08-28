package com.ericsson.ci.taf.thomas.example;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;

import com.ericsson.cifwk.taf.data.DataHandler;

public class SimpleExample {

	public static void main(String[] args) throws ConfigurationException{
		System.out.println("Create Config Factory");
		ConfigurationFactory factory = new ConfigurationFactory("config.xml");
		System.out.println("Create Config object");
		Configuration config = factory.getConfiguration();
		
		Iterator<String> keys = config.getKeys();
		
		System.out.println("Start printing properties");
		while(keys.hasNext()){
			String key = keys.next();
			System.out.println("Key is: "+key+"\tValue is: "+config.getProperty(key));
		}
		
		System.out.println("===========================================\n\n");
		
		Map result = DataHandler.getAttributes();
			
		Iterator<String> mapKeys = result.keySet().iterator();
		
		while(mapKeys.hasNext()){
			String key = mapKeys.next();
			System.out.println("Key is: "+key+"\tValus is: "+result.get(key));
		}

	}
}
