package com.ericsson.cifwk.taf.demo.calculator.test.cases;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.demo.calculator.test.helpers.CalculatorOperatorHelper;
import com.ericsson.cifwk.taf.demo.calculator.test.operators.CalculatorOperator;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class CalculatorTest extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<CalculatorOperator> calculatorProvider;

	@TestId(id = "taftest12-func-1", title = "Verify calculator can add as expected")
	@Context(context = { Context.UI })
	@Test
	@DataDriven(name = "calculator_add")
	public void verifyCalculatorAddsNumbersAsExpected(
			@Input("initialNumber") String initialNumber,
			@Input("additive") String additive,
			@Output("expected") String expected) {

		CalculatorOperator calculator = getOperator();
		String result = calculator.add(initialNumber, additive);
		verifyResult(expected, result);
	}

	@TestId(id = "taftest12-func-2", title = "Verify calculator can subtract as expected")
	@Context(context = { Context.UI })
	@Test
	@DataDriven(name = "calculator_subtract")
	public void verifyCalculatorSubtractsNumbersAsExpected(
			@Input("minuend") String minuend,
			@Input("subtrahend") String subtrahend,
			@Output("expected") String expected) {

		CalculatorOperator calculator = getOperator();
		String result = calculator.subtract(minuend, subtrahend);
		verifyResult(expected, result);
	}

	@TestId(id = "taftest13-func-1", title = "Verify calculator can multiply as expected")
	@Context(context = {Context.UI })
	@Test
	@DataDriven(name = "calculator_multiply")
	public void verifyCalculatorMultipliesNumbersAsExpected(
			@Input("multiplicand1") String multiplicand1,
			@Input("multiplicand2") String multiplicand2,
			@Output("expected") String expected) throws Exception {

		CalculatorOperator calculator = getOperator();
		String result = calculator.multiply(multiplicand1, multiplicand2);
		verifyResult(expected, result);
	}

	@TestId(id = "taftest14-func-1", title = "Verify calculator can divide as expected")
	@Context(context = { Context.UI })
	@Test
	@DataDriven(name = "calculator_divide")
	public void verifyCalculatorDividesNumbersAsExpected(
			@Input("dividend") String dividend,
			@Input("dividor") String dividor,
			@Output("expected") String expected) {

		CalculatorOperator calculator = getOperator();
		String result = calculator.divide(dividend, dividor);
		verifyResult(expected, result);
	}

	private CalculatorOperator getOperator() {
		return calculatorProvider.provide(CalculatorOperator.class);
	}

	private void verifyResult(String expected, String result) {
		if (!CalculatorOperatorHelper.isNumeric(result)) {
			assertEquals(result, expected);
		} else {
			assertEquals(0, new BigDecimal(result).compareTo(new BigDecimal(expected)));
		}
	}

}
