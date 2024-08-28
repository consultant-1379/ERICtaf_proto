package com.ericsson.cifwk.taf.api.prototype.operators;

import com.ericsson.cifwk.taf.api.prototype.getters.GreeterGetter;
import com.ericsson.cifwk.taf.api.prototype.operators.api.ApiGreeterOperatorTest;

public class GreeterOperator {

	ApiGreeterOperatorTest apiOperator = new ApiGreeterOperatorTest();
	public String expectedGreeting(String name){
		return GreeterGetter.GREETING + name + GreeterGetter.GREETING_END;
	}
	
	public String greeting(String name){
		return apiOperator.greeting(name);
	}
}
