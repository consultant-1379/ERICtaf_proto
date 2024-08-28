package com.ericsson.duraci.test.helpers;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.datawrappers.Request;
import com.ericsson.duraci.datawrappers.Response;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.TestSampleEvent;

public class SampleEventBuilder {
	private static final String NUMERAL_SUBSTITUTE = "X";
	private static final String THREAD_NAME_PATTERN = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.optionalParameters.thread");
	private static final String LATENCY_RANGE = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.optionalParameters.latency");
	private static final String RESPONSE_DATA_PATTERN = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.data");
	private static final String RESPONSE_SIZE_RANGE = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.size");
	private static final String TIME_SPENT_IN_MILLIS = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.timeSpentInMillis");
	private static final String RESPONSE_SUCCESS_PROBABILITY = EventBuilderHelper.getProperty("eventGen.sampleEvent.response.successProbability");
	private static final String REQUEST_TARGET_PATTERN = EventBuilderHelper.getProperty("eventGen.sampleEvent.request.target");
	private static final String REQUEST_DATA_PATTERN = EventBuilderHelper.getProperty("eventGen.sampleEvent.request.data");
	private static final String REQUEST_SIZE_RANGE = EventBuilderHelper.getProperty("eventGen.sampleEvent.request.size");
	
	private Random rnd = new Random();
	private final Pattern numberPlaceholder = Pattern.compile("(<<" + NUMERAL_SUBSTITUTE + "+>>)");
	
	public TestSampleEvent createTestSampleEvent(ExecutionId testCaseExecutionId, long sampleId) {
		Request request = createSampleRequest();
		Response response = createSampleResponse();

		TestSampleEvent event = TestSampleEvent.Factory.create(null, null, new ExecutionId(), testCaseExecutionId, request, response, 
				"HTTP", sampleId);

		populateEvent(event);
		
		return event;
	}

	private void populateEvent(TestSampleEvent event) {
		event.setOptionalParameter("thread", autoReplacePlaceholders(THREAD_NAME_PATTERN));
		event.setOptionalParameter("latency", String.valueOf(randomValueInRange(LATENCY_RANGE)));
	}

	Response createSampleResponse() {
		Response response = new Response();
		response.setData(autoReplacePlaceholders(RESPONSE_DATA_PATTERN));
		double successProbability = Double.parseDouble(RESPONSE_SUCCESS_PROBABILITY);
		response.setResult(EventBuilderHelper.getRandomResultCode(successProbability));
		response.setSize(randomValueInRange(RESPONSE_SIZE_RANGE));
		response.setTime(randomValueInRange(TIME_SPENT_IN_MILLIS));
		response.setType("200");
		
		return response;
	}

	Request createSampleRequest() {
		Request request = new Request();
		request.setTarget(autoReplacePlaceholders(REQUEST_TARGET_PATTERN));
		request.setData(autoReplacePlaceholders(REQUEST_DATA_PATTERN));
		request.setSize(randomValueInRange(REQUEST_SIZE_RANGE));
		request.setType(getRandomHttpRequestMethod());
		
		return request;
	}

	String autoReplacePlaceholders(String propertyValue) {
		String result = autoReplaceNumberPlaceholders(propertyValue);
		result = autoReplaceGuidPlaceholders(result);
		return result;
	}

	String autoReplaceNumberPlaceholders(String propertyValue) {
		String result = propertyValue;
		for (Matcher matcher = numberPlaceholder.matcher(result); matcher.find(0);) {
			int startIndex = matcher.start(1);
			int endIndex = matcher.end(1);
			String placeholder = StringUtils.substring(result, startIndex, endIndex);
			int numeralCount = StringUtils.countMatches(placeholder, NUMERAL_SUBSTITUTE);
			int randomInt = rnd.nextInt((int)(Math.pow(10, numeralCount) - 2)) + 1;
			result = matcher.replaceFirst(String.valueOf(randomInt));
			matcher.reset(result);
		}

		return result;
	}

	String autoReplaceGuidPlaceholders(String requestTargetPattern) {
		return requestTargetPattern.replaceAll("<<GUID>>", UUID.randomUUID().toString());
	}

	int randomValueInRange(String requestSizeRange) {
		String[] range = requestSizeRange.split("\\-");
		int min = Integer.parseInt(range[0]);
		int max = Integer.parseInt(range[1]);
		
		if (min >= max) {
			throw new RuntimeException("Invalid integer value range: '" + requestSizeRange + "'");
		}
		
		return rnd.nextInt((max - min) + 1) + min;
	}

	private String getRandomHttpRequestMethod() {
		return (rnd.nextInt() % 2 == 0) ? "GET" : "POST";
	}
}
