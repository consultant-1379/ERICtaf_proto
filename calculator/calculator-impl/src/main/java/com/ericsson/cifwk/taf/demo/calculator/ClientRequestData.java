package com.ericsson.cifwk.taf.demo.calculator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientRequestData {

	
	double variableOne;
	double variableTwo;
	
	
	public double getVariableOne() {
		return variableOne;
	}
	public void setVariableOne(double variableOne) {
		this.variableOne = variableOne;
	}
	public double getVariableTwo() {
		return variableTwo;
	}

	public void setVariableTwo(double variableTwo) {
		this.variableTwo = variableTwo;
	}

}
