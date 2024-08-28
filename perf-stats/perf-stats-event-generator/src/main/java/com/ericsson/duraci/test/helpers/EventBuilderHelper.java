package com.ericsson.duraci.test.helpers;

import java.util.Random;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.duraci.datawrappers.ResultCode;

public abstract class EventBuilderHelper {
	private EventBuilderHelper() {}
	
	public static ResultCode getRandomResultCode(double successProbability) {
		if( Math.random() <= successProbability ) {
			return ResultCode.SUCCESS;
		} 

		ResultCode[] allCodes = ResultCode.allRecognized;
		Random rand = new Random();
		return allCodes[rand.nextInt(allCodes.length)];
	}
	
	public static String getProperty(String name) {
		return (String) DataHandler.getAttribute(name);
	}


}
