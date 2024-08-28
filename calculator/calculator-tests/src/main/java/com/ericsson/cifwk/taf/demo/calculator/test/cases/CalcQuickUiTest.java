package com.ericsson.cifwk.taf.demo.calculator.test.cases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.demo.calculator.test.operators.CalculatorOperator;
import com.ericsson.cifwk.taf.demo.calculator.test.operators.CalculatorUiOperator;

// For testware check purposes
public class CalcQuickUiTest extends TorTestCaseHelper implements TestCase {
	private CalculatorUiOperator operator;

	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(enabled = true)
	public void addHappyPath() {
		operator = new CalculatorUiOperator(/*"http://localhost:8080/calculator/index.html"*/);

		Assert.assertEquals("4.64", operator.add("1.2", "3.44"));
		Assert.assertEquals(false, operator.isValidationError());
		Assert.assertEquals(CalculatorOperator.OPERATION_FAILED, operator.add("a", "3.44"));
		Assert.assertEquals(true, operator.isValidationError());
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(enabled = true)
	public void subtractHappyPath() {
		operator = new CalculatorUiOperator(/*"http://localhost:8080/calculator/index.html"*/);

		Assert.assertEquals("2", operator.subtract("5", "3"));
		Assert.assertEquals(false, operator.isValidationError());
		Assert.assertEquals(CalculatorOperator.OPERATION_FAILED, operator.add("a", "3.44"));
		Assert.assertEquals(true, operator.isValidationError());
	}
}
