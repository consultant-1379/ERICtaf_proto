package com.ericsson.cifwk.taf.demo.calculator.test.operators;

public interface CalculatorOperator {
	final String OPERATION_FAILED = "ERROR";
	final String DIVISION_BY_ZERO_ATTEMPT = "DIV/0";
	
	String add(String variableOne, String variableTwo);
	String subtract(String variableOne, String variableTwo);
	String multiply(String variableOne, String variableTwo);
	String divide(String variableOne, String variableTwo);
}
