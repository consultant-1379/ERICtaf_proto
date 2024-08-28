package com.ericsson.cifwk.taf.demo.calculator.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.ericsson.cifwk.taf.demo.calculator.api.Calculator;
import com.ericsson.cifwk.taf.demo.calculator.api.DivisionByZeroException;

@Stateless
@Remote(Calculator.class)
public class CalculatorImpl implements Calculator {

	public BigDecimal add(double initialNumber, double additive) {
		BigDecimal initialNumberBig = BigDecimal.valueOf(initialNumber);
		BigDecimal additiveBig = BigDecimal.valueOf(additive);
		
		return initialNumberBig.add(additiveBig).stripTrailingZeros();
	}

	public BigDecimal subtract(double minuend, double subtrahend) {
		BigDecimal minuendBig = BigDecimal.valueOf(minuend);
		BigDecimal subtrahendBig = BigDecimal.valueOf(subtrahend);

		return minuendBig.subtract(subtrahendBig).stripTrailingZeros();
	}

	public BigDecimal divide(double dividend, double dividor) {
		BigDecimal dividendBig = BigDecimal.valueOf(dividend);
		
		if (dividor == 0) {
			throw new DivisionByZeroException();
		}
		
		BigDecimal dividorBig = BigDecimal.valueOf(dividor);

		return dividendBig.divide(dividorBig).stripTrailingZeros();
	}

	public BigDecimal multiply(double multiplicand1, double multiplicand2) {
		BigDecimal multiplicand1Big = BigDecimal.valueOf(multiplicand1);
		BigDecimal multiplicand2Big = BigDecimal.valueOf(multiplicand2);

		return multiplicand1Big.multiply(multiplicand2Big).stripTrailingZeros();
	}
}
