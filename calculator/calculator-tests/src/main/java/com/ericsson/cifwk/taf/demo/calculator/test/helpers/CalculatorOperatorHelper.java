package com.ericsson.cifwk.taf.demo.calculator.test.helpers;

public abstract class CalculatorOperatorHelper {
	public static boolean isNumeric(String value) {
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
