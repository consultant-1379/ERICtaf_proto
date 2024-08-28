package com.ericsson.ci.taf.thomas.example.json.config.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.DefaultConfigurationNode;
import org.apache.commons.configuration.tree.DefaultExpressionEngine;
import org.apache.commons.configuration.tree.ExpressionEngine;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TafJsonParser {
	
	private static Logger log = Logger.getLogger(TafJsonParser.class);

	private JsonNode rootNode;

	public TafJsonParser(Reader in) {
		ObjectMapper mapper = new ObjectMapper();
		try {
		rootNode = mapper.readTree(in);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JsonConfiguration parse() {
		List children = new ArrayList();
		Iterator<JsonNode> elements = rootNode.iterator();
		
		DefaultConfigurationNode rootLevel = new DefaultConfigurationNode();
		rootLevel.setName("host");
		while(elements.hasNext()){
			JsonNode host = elements.next();
			System.out.println("Child is: "+host.toString());

			DefaultConfigurationNode hostName = new DefaultConfigurationNode(host.get("hostname").asText());

			DefaultConfigurationNode ip = new DefaultConfigurationNode("ip", host.get("ip").asText());
			hostName.addChild(ip);
			DefaultConfigurationNode type = new DefaultConfigurationNode("type", host.get("type").asText());
			hostName.addChild(type);
			DefaultConfigurationNode usersNode = new DefaultConfigurationNode("user");
			JsonNode usersList = host.get("users");
			Iterator<JsonNode> userElements = usersList.elements();
			while(userElements.hasNext()){
				JsonNode user = userElements.next();
				DefaultConfigurationNode userName = new DefaultConfigurationNode(user.get("username").asText());
				DefaultConfigurationNode password = new DefaultConfigurationNode("pass",user.get("password").asText());
				userName.addChild(password);
				JsonNode typeValue = user.get("type");
				if(typeValue != null){ 
					DefaultConfigurationNode userType = new DefaultConfigurationNode("type",typeValue.asText()); 
					userName.addChild(userType);
				}
				usersNode.addChild(userName);
			}
			hostName.addChild(usersNode);
			
			DefaultConfigurationNode portsNode = new DefaultConfigurationNode("port");
			Iterator<Entry<String, JsonNode>> portsElements = host.get("ports").fields();
			while(portsElements.hasNext()){
				Entry<String, JsonNode> port = portsElements.next();
				DefaultConfigurationNode portNode = new DefaultConfigurationNode(port.getKey(),port.getValue().asInt());
				portsNode.addChild(portNode);
				
			}
			hostName.addChild(portsNode);
			rootLevel.addChild(hostName);

		}
		
		JsonConfiguration configuration = new JsonConfiguration();
        ConfigurationNode root = configuration.getRootNode();
        root.addChild(rootLevel);

		return configuration;
	}

}
