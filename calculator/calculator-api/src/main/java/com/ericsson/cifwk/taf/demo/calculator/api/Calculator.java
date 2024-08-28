package com.ericsson.cifwk.taf.demo.calculator.api;

import java.math.BigDecimal;

public interface Calculator {

	BigDecimal add(double initialNumber, double additive);

	BigDecimal subtract(double minuend, double subtrahend);

	/**
	 * Throws <code>com.ericsson.cifwk.taf.demo.calculator.api.DivisionByZeroException</code> if 
	 * division by zero is attempted.
	 * 
	 * @param dividend
	 * @param dividor
	 * @return
	 */
	BigDecimal divide(double dividend, double dividor);

	BigDecimal multiply(double multiplicand1, double multiplicand2);

}
