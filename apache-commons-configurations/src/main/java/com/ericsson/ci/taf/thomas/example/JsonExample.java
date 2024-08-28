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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExample {

	public static void main(String[] args) throws ConfigurationException, IOException{
		System.out.println("Create Config Factory");
		CompositeConfiguration config = new CompositeConfiguration();
		config.addConfiguration(new SystemConfiguration());
		config.addConfiguration(new PropertiesConfiguration("default_taffit_hosts.properties"));
//		config.addConfiguration(new PropertyListConfiguration("json_format.json"));
		System.out.println("Create Config object");
		File file = new File("/home/ethomev/Work/gitrepos/ERICtaf_proto/apache-commons-configurations/src/main/resources/json_format.json");
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		while( ( line = br.readLine() ) != null ) {
	        stringBuilder.append( line );
	    }
		
		System.out.println("JSON IS: \n"+stringBuilder.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(file);
		System.out.println(node.toString());
		Iterator<JsonNode> children = node.iterator();
		
		while(children.hasNext()){
			Map <String, Object> hostObject = new HashMap<String, Object>();
			JsonNode host = children.next();
			System.out.println("Child is: "+host.toString());
			
			String hostname = host.get("hostname").toString();
			hostname = hostname.substring(1, hostname.length()-1);
			System.out.println(hostname);
			
			String prefix = "host."+hostname;
			
			String ip = host.get("ip").toString();
			ip = ip.substring(1, ip.length()-1);
			System.out.println(ip);
			hostObject.put(prefix+".ip", ip);
			
			String type = host.get("type").toString();
			type = type.substring(1, type.length()-1);
			System.out.println(type);
			hostObject.put(prefix+".type", type);
			
			JsonNode users = host.get("users");
			System.out.println(users);
			hostObject.put(prefix+".users", users);
			
			Iterator<Entry<String, JsonNode>> ports = host.get("ports").fields();
			
			String portPrefix = prefix+".port";
			while(ports.hasNext()){
				Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) ports.next();
				String key = entry.getKey();
				String value = entry.getValue().toString();
				hostObject.put(portPrefix+"."+key, value);
				
			}
			
//			System.out.println(ports);
//			hostObject.put(prefix+".port", ports);
			
			JsonNode nodes = host.get("nodes");
			System.out.println(nodes);
			hostObject.put(prefix+".node", nodes);
			
			config.addConfiguration(new MapConfiguration(hostObject));
			
			
			
//			Iterator<JsonNode> grandChild = key.iterator();
//			while(grandChild.hasNext()){
//				JsonNode subKey = grandChild.next();
//				if(subKey.isValueNode()){
//					
//				}
//				System.out.println("Child is: "+subKey.toString());
//			}
		}
		
		Iterator<String> keys = config.getKeys();
		
		System.out.println("Start printing properties");
		while(keys.hasNext()){
			String key = keys.next();
			System.out.println("Key is: "+key+"\tValue is: "+config.getProperty(key));
		}
			
		

	}
}
