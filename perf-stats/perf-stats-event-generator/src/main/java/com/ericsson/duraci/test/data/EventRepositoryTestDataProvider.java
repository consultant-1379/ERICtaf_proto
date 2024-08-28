package com.ericsson.duraci.test.data;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.cifwk.taf.data.DataHandler;

/**
 * 
 * Test DataProvider for executing Test Cases for EventRepository
 */

public class EventRepositoryTestDataProvider implements TestData {

	public static boolean getRestCallProperty(){
    	boolean makeRestCall = false;
    	try {
	        if ("yes".equalsIgnoreCase(DataHandler.getAttribute("makeRestCall").toString())){
		        makeRestCall = true;
	        }
        } catch (NullPointerException e){
        	System.err.println("Cannot find Property: \"makeRestCall\" in run.properties file");
        	e.printStackTrace();
        }
    	return makeRestCall;
    }
	
	public static boolean getUpstreamRestCallProperty(){
    	boolean makeRestCall = false;
    	try {
	        if ("yes".equalsIgnoreCase(DataHandler.getAttribute("makeUpstreamRestCall").toString())){
		        makeRestCall = true;
	        }
        } catch (NullPointerException e){
        	System.err.println("Cannot find Property: \"makeUpstreamRestCall\" in run.properties file");
        	e.printStackTrace();
        }
    	return makeRestCall;
    }
	
	
	public static double getDelayProperty(String property){
		double delay  = 0;
		try {
			delay = Double.parseDouble(DataHandler.getAttribute(property).toString());
			if (property.equals("plusOrMinus")){
				double primary = getDelayProperty("primaryDelay");
				if (delay > primary){
					delay = primary;
				}
			}
			if (delay < 0){
				delay = 0;
			}
		}catch (NumberFormatException e) {
			System.err.printf("Cannot find Property: \"%s\" in run.properties file\nDelay set to 0 seconds", property);
			e.printStackTrace();
		}
		return delay;
	}
	
	public static double getRestCallRate(){
		double rate  = 1;
		try {
			rate = Double.parseDouble(DataHandler.getAttribute("restCallRate").toString());
			if (rate < 0){
				rate = 0;
			}
			else if (rate > 1){
				rate = 1;
			}
		}catch (NumberFormatException e) {
			System.err.println("Cannot find Property: \"restCallRate\" in run.properties file\nAll events will be queried over Rest");
			e.printStackTrace();
		}
		return rate;
	}
}
