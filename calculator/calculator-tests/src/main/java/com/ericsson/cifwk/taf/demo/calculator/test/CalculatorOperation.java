package com.ericsson.cifwk.taf.demo.calculator.test;

public enum CalculatorOperation {
	ADD("add"), SUBTRACT("subtract"), MULTIPLY("multiply"), DIVIDE("divide");

	private String value; 
	
	CalculatorOperation(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}
