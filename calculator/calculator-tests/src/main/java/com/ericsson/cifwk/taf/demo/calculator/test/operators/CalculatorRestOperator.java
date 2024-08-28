package com.ericsson.cifwk.taf.demo.calculator.test.operators;

import java.util.List;

import javax.inject.Singleton;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.demo.calculator.test.CalculatorOperation;
import com.ericsson.cifwk.taf.tal.rest.RestResponseCode;
import com.ericsson.cifwk.taf.tools.RestTool;

@Operator(context = Context.REST)
@Singleton
public class CalculatorRestOperator implements CalculatorOperator {

	public String add(String initialNumber, String additive) {
		return callRestOperation(initialNumber, additive, CalculatorOperation.ADD);
	}

	public String subtract(String variableOne, String variableTwo) {
		return callRestOperation(variableOne, variableTwo, CalculatorOperation.SUBTRACT);
	}

	public String multiply(String variableOne, String variableTwo) {
		return callRestOperation(variableOne, variableTwo, CalculatorOperation.MULTIPLY);
	}

	public String divide(String variableOne, String variableTwo) {
		return callRestOperation(variableOne, variableTwo, CalculatorOperation.DIVIDE);
	}

	private String callRestOperation(String value1, String value2, CalculatorOperation operation) {
		Host restServer = DataHandler.getHostByName("calculator");
		RestTool restTool = new RestTool(restServer);
		String input = String.format("{\"variableOne\":%s,\"variableTwo\":%s}", 
				value1, value2);

		List<String> response = restTool.postJson("calculator/rest/" + operation.value(), input, false);
		
		// Check if service was invoked successfully
		if (!isInvokedSuccessfully(restTool)) {
			String responseToProcess = response.get(0);
			if (StringUtils.contains(responseToProcess, "DivisionByZeroException")) {
				return CalculatorOperator.DIVISION_BY_ZERO_ATTEMPT;
			} else {
				return CalculatorOperator.OPERATION_FAILED;
			}
		}
		
		if (response.isEmpty()) {
			throw new RuntimeException("REST call response was empty");
		}
		String jsonString = preProcessCurlResponse(response);

		return extractResultFromJSon(jsonString);
	}

	private boolean isInvokedSuccessfully(RestTool restTool) {
		List<RestResponseCode> lastResponseCodes = restTool.getLastResponseCodes();
		if (CollectionUtils.isEmpty(lastResponseCodes)) {
			return false;
		}
		
		for (RestResponseCode responseCode : lastResponseCodes) {
			if (!RestResponseCode.OK.equals(responseCode)) {
				return false;
			}
		}
		
		return true;
	}

	private String extractResultFromJSon(String jsonString) {
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) JSONSerializer.toJSON(jsonString);
			return jsonObject.getString("result");
		} catch (Exception e) {
			throw new RuntimeException(String.format("Failed to parse REST response '%s'", jsonString), e);
		}
	}

	// This method is needed to pre-process JSON response that comes via cURL
	// because cURL adds some special symbols to JSON.
	private String preProcessCurlResponse(List<String> response) {
		String responseToProcess = response.get(0);
		int jsonStart = responseToProcess.indexOf("{");
		int jsonEnd = responseToProcess.indexOf("}");
		String jsonString = responseToProcess.substring(jsonStart, jsonEnd + 1);

		jsonString = jsonString.replaceFirst("!!float", "");
		jsonString = jsonString.replaceAll("'", "");
		return jsonString;
	}
}
