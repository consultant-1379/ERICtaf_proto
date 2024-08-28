package com.ericsson.cifwk.taf.demo.calculator.ejb;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CalculatorImplTest {

	@Test
	public void addTest() throws Exception {
		CalculatorImpl calculatorImpl = new CalculatorImpl();

		BigDecimal result = calculatorImpl.add(2.3, 3.23);

		Assert.assertEquals("5.53", result.toString());
		Assert.assertEquals(new BigDecimal("5.53"), result);
		
		result = calculatorImpl.add(2, 3);
		Assert.assertEquals("5", result.toString());
	}

	@Test
	@Ignore
	public void subtractTest() throws Exception {
		CalculatorImpl calculatorImpl = new CalculatorImpl();

		BigDecimal result = calculatorImpl.subtract(5.5, 2.5);
		
		Assert.assertEquals(new BigDecimal("3.0"), result);
	}

	@Test
	@Ignore
	public void divideTest() throws Exception {
		CalculatorImpl calculatorImpl = new CalculatorImpl();

		BigDecimal result = calculatorImpl.divide(6.0, 3.0);

		Assert.assertEquals(new BigDecimal("2.0"), result);

	}

	@Test
	@Ignore
	public void multiplyTest() throws Exception {
		CalculatorImpl calculatorImpl = new CalculatorImpl();

		BigDecimal result = calculatorImpl.multiply(6.0, 3.0);

		Assert.assertEquals(new BigDecimal("18.0"), result);
	}
}
