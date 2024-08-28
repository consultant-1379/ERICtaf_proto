package com.ericsson.ci.taf.thomas.example;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;

public class SimpleExample {

	public static void main(String[] args){
		System.out.println("Create Config Factory");
		ConfigurationFactory factory = new ConfigurationFactory("config.xml");
		System.out.println("Create Config object");
		Configuration config = factory.getConfiguration();
//		
//		Iterator<String> keys = config.getKeys();
//		
//		System.out.println("Start printing properties");
//		while(keys.hasNext()){
//			String key = keys.next();
//			System.out.println("Key is: "+key+"\tValue is: "+config.getProperty(key));
//		}
//			
		

	}
}
