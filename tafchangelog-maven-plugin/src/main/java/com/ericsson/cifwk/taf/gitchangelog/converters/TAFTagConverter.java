package com.ericsson.cifwk.taf.gitchangelog.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.cifwk.taf.gitchangelog.converters.MessageConverter;
import org.apache.maven.plugin.logging.Log;

public class TAFTagConverter implements MessageConverter {
	
	private final Log log;	

	public TAFTagConverter(Log log) {
		this.log = log;	
	}

	public String formatMessage(String original) {
	    try {    
		 Pattern pat = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		 Matcher mat = pat.matcher(original);
		 if(mat.find()) {
			 return mat.group();
		 }
		 Pattern pat2 = Pattern.compile("\\d{1,3}\\.\\d{1,3}");
		 mat = pat2.matcher(original);
		 if(mat.find()) {
			 return mat.group();
		 }
		 return null;
		} catch (Exception e) {
			// log, but don't let this small setback fail the build
			log.info("Unable to parse the tag " + original, e);
		}
		return null;
	}
}
