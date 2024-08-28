package com.ericsson.cifwk.taf.demo.calculator.test.operators;

import java.math.BigDecimal;

import javax.ejb.EJBException;
import javax.inject.Singleton;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.demo.calculator.api.Calculator;
import com.ericsson.cifwk.taf.demo.calculator.api.DivisionByZeroException;
import com.ericsson.cifwk.taf.demo.calculator.test.CalculatorOperation;
import com.ericsson.cifwk.taf.handlers.AsRmiHandler;

@Operator(context = Context.API)
@Singleton
public class CalculatorEjbOperator implements CalculatorOperator {
	
	private Calculator locateEjb() {
		Host server = DataHandler.getHostByName("calculator");
		String jndiString = (String) DataHandler.getAttribute("calculator.jndi");

		try {
			AsRmiHandler asRmiHandler = new AsRmiHandler(server);
			return (Calculator) asRmiHandler.getServiceViaJndiLookup(jndiString);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to locate Calculator EJB!", e);
		}
	}

	public String add(String value1AsString, String value2AsString) {
		return performOperation(value1AsString, value2AsString, CalculatorOperation.ADD);
	}

	public String subtract(String value1AsString, String value2AsString) {
		return performOperation(value1AsString, value2AsString, CalculatorOperation.SUBTRACT);
	}

	public String multiply(String value1AsString, String value2AsString) {
		return performOperation(value1AsString, value2AsString, CalculatorOperation.MULTIPLY);
	}

	public String divide(String value1AsString, String value2AsString) {
		return performOperation(value1AsString, value2AsString, CalculatorOperation.DIVIDE);
	}

	private String performOperation(String value1AsString, String value2AsString, CalculatorOperation operation) {
		double value1;
		double value2;
		try {
			value1 = Double.parseDouble(value1AsString);
			value2 = Double.parseDouble(value2AsString);
		} catch (NumberFormatException e) {
			return CalculatorOperator.OPERATION_FAILED;
		}

		return invokeEjb(value1, value2, operation);
	}
	
	private String invokeEjb(double value1, double value2, CalculatorOperation operation) {
		BigDecimal result = null;
		// Need to look it up each time, otherwise JBoss starts to throw exceptions with EJBCLIENT000025 error
		Calculator calculatorEjb = locateEjb();
		switch (operation) {
		case ADD:
			result = calculatorEjb.add(value1, value2);
			break;
		case SUBTRACT:
			result = calculatorEjb.subtract(value1, value2);
			break;
		case MULTIPLY:
			result = calculatorEjb.multiply(value1, value2);
			break;
		case DIVIDE:
			try {
				result = calculatorEjb.divide(value1, value2);
			} catch (EJBException e) {
				if (e.getCause() != null && e.getCause() instanceof DivisionByZeroException) {
					return CalculatorOperator.DIVISION_BY_ZERO_ATTEMPT;
				} else {
					return CalculatorOperator.OPERATION_FAILED;
				}
			}
			break;
		default:
			throw new UnsupportedOperationException(
					String.format("Operation '%s' is not supported yet", operation.toString()));		
		}
		
		return result.toString();
	}
}
