package com.ericsson.duraci.test.cases;

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.datawrappers.ResultCode;
import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobFinishedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobStartedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobStepFinishedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobStepStartedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelTestCaseFinishedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelTestCaseStartedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelTestSuiteFinishedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelTestSuiteStartedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.TestSampleEvent;
import com.ericsson.duraci.eiffelmessage.sending.MessageSender;
import com.ericsson.duraci.logging.JavaLoggerEiffelLog;
import com.ericsson.duraci.test.config.PerformanceTestEiffelConfiguration;
import com.ericsson.duraci.test.config.SilentEiffelLogger;
import com.ericsson.duraci.test.data.EventRepositoryTestDataProvider;
import com.ericsson.duraci.test.helpers.EventBuilderHelper;
import com.ericsson.duraci.test.helpers.SampleEventBuilder;
import com.ericsson.duraci.test.operators.EventRepositoryOperator;
import com.ericsson.duraci.test.operators.EventRepositoryOperatorAPI;
import com.google.common.collect.Lists;

public class EventRepositoryTest implements TestCase {
	private static final int JOB_COUNT = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.jobCount"));
	private static final int JOB_STEP_COUNT = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.jobStepCount"));
	private static final int TEST_SUITE_COUNT = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.testSuiteCount"));
	private static final int TEST_CASE_COUNT = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.testCaseCount"));
	private static final int SAMPLE_COUNT = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.sampleCount"));
	private static final int EXPECTED_JOB_STEP_DURATION_IN_SECS = Integer.parseInt(EventBuilderHelper.getProperty("eventGen.expectedJobStepDurationInSeconds"));
	private static final double TEST_SUCCESS_PROBABILITY = Double.parseDouble(EventBuilderHelper.getProperty("eventGen.testSuccessProbability"));

	private static final Logger log = LoggerFactory.getLogger(EventRepositoryTest.class);

	private EventRepositoryOperator operator = new EventRepositoryOperatorAPI();
	private SampleEventBuilder sampleEventBuilder = new SampleEventBuilder();
	private JavaLoggerEiffelLog logger = new SilentEiffelLogger(EventRepositoryTest.class);
	private ThreadLocal<MessageSender> senderContainer = new ThreadLocal<MessageSender>();

	@BeforeMethod
	public void setUp() {
		if (senderContainer.get() == null) {
			senderContainer.set(new MessageSender.Factory(logger, new PerformanceTestEiffelConfiguration()).create());
		}
	}

	@AfterMethod
	public void tearDown() {
		MessageSender sender = senderContainer.get();
		if (sender != null) {
			sender.dispose();
			senderContainer.set(null);
		}
	}

	/**
	 * Tests that the ER can receive a single EiffelJobStartedEvent from the MB.
	 * <p>
	 * This is performed by creating and sending the event and then retrieving it via REST.
	 * <p>
	 * Utilizes TAF's Data Driven approach.
	 * Test case details can be edited and new tests added by editing src/main/resources/eventrepo.csv
	 * <p>
	 * The REST call option can be toggled on/off by editing src/main/resources/taf_properties/run.properties.
	 * If this option is disabled, the existence of the sent event in the ER cannot be verified.
	 * This enables the test case to function as a load generator.
	 * <p>
	 * @param jobInstance
	 * @param jobExecutionId
	 * @param jobExecutionNumber
	 */
	@Context(context = { Context.API })
	@DataDriven(name = "eventrepo")
	@Test(enabled = false)
	public void oneEventSendAndReceive(@Input("jobInstance") String jobInstance, @Input("jobExecutionId") String jobExecutionId, @Input("jobExecutionNumber") int jobExecutionNumber) {
		EiffelEvent event = EiffelJobStartedEvent.Factory.create(jobInstance, jobExecutionId, jobExecutionNumber);
		boolean result = operator.sendMessage(event);
		if (EventRepositoryTestDataProvider.getRestCallProperty()) {
			Assert.assertTrue(result);
		}
	}

//	@Context(context = { Context.API })
//	@Test(enabled = true)
//	public void generateCIP_4181Events() {
//		long startTime = System.currentTimeMillis();
//
//		// Sending 'started' events
//		EiffelJobStartedEvent[] jobStartedEvents = storeJobStartedEvents(JOB_COUNT);
//		List<EiffelJobStepStartedEvent> jobStepStartedEvents = storeJobStepStartedEvents(JOB_STEP_COUNT, jobStartedEvents);
//		List<EiffelTestSuiteStartedEvent> testSuiteStartedEvents = storeTestSuiteStartedEvents(TEST_SUITE_COUNT, jobStepStartedEvents);
//		List<EiffelTestCaseStartedEvent> testCaseStartedEvents = storeTestCaseStartedEvents(TEST_CASE_COUNT, testSuiteStartedEvents);
//
//		// Sending sample events
//		storeSampleEvents(SAMPLE_COUNT, testCaseStartedEvents);
//
//		// Sending 'finished' events
//		storeTestCaseFinishedEvents(TEST_CASE_COUNT, testCaseStartedEvents);
//		storeTestSuiteFinishedEvents(TEST_SUITE_COUNT, testSuiteStartedEvents);
//		storeJobStepFinishedEvents(JOB_STEP_COUNT, jobStepStartedEvents);
//		storeJobFinishedEvents(JOB_COUNT, jobStartedEvents);
//
//		DecimalFormat numberFormatter = new DecimalFormat("#,###");
//		int totalEventNumber = (JOB_COUNT * 2) + (JOB_STEP_COUNT * 2) + (TEST_SUITE_COUNT * 2) + (TEST_CASE_COUNT * 2) + SAMPLE_COUNT;
//		log.info(String.format("%s events were sent in %d millis", 
//				numberFormatter.format(totalEventNumber), System.currentTimeMillis() - startTime));
//	}

	@Context(context = { Context.API })
	@Test(enabled = true)
	public void generateCIP_4181Events() {
		long startTime = System.currentTimeMillis();

		for (int i=0; i<JOB_COUNT; i++) {
			EiffelJobStartedEvent jobStartedEvent = createAndSendJobStartedEvent(i);
			for (int j=0; j<JOB_STEP_COUNT; j++) {
				EiffelJobStepStartedEvent jobStepStartedEvent = createAndSendJobStepStartedEvent(jobStartedEvent.getJobExecutionId());
				for (int k=0; k<TEST_SUITE_COUNT; k++) {
					EiffelTestSuiteStartedEvent testSuiteStartedEvent = createAndSendTestSuiteStartedEvent(jobStepStartedEvent.getJobStepExecutionId(), 
							"Performance test type", "Performance test suite", k);
					for (int l=0; l<TEST_CASE_COUNT; l++) {
						EiffelTestCaseStartedEvent testCaseStartedEvent = createAndSendTestCaseStartedEvent(testSuiteStartedEvent.getTestSuiteExecutionId(), 
								"Test #" + System.currentTimeMillis(), "Performance test", l);
						createAndSendSampleEvents(testCaseStartedEvent.getTestCaseExecutionId(), SAMPLE_COUNT);
						createAndSendTestCaseFinishedEvent(testCaseStartedEvent);
					}
					createAndSendTestSuiteFinishedEvent(testSuiteStartedEvent);
				}
				createAndSendJobStepFinishedEvent(jobStepStartedEvent);
			}
			createAndSendJobFinishedEvent(jobStartedEvent);
		}

		DecimalFormat numberFormatter = new DecimalFormat("#,###");
		int totalEventNumber = (JOB_COUNT * 2) + (JOB_STEP_COUNT * 2) + (TEST_SUITE_COUNT * 2) + (TEST_CASE_COUNT * 2) + SAMPLE_COUNT;
		log.info(String.format("%s events were sent in %d millis", 
				numberFormatter.format(totalEventNumber), System.currentTimeMillis() - startTime));
	}

	private EiffelJobStartedEvent[] storeJobStartedEvents(int jobCount) {
		log.info(String.format("Will create and send %d instances of %s", jobCount, EiffelJobStartedEvent.class.getName()));
		EiffelJobStartedEvent[] jobStartedEvents = createAndSendJobStartedEvents(jobCount);
		log.info("Done");
		return jobStartedEvents;
	}

	private void storeJobFinishedEvents(int jobCount, EiffelJobStartedEvent[] jobStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'job finished event'", jobCount, EiffelJobFinishedEvent.class.getName())); 
		for (EiffelJobStartedEvent jobStartedEvent : jobStartedEvents) {
			createAndSendJobFinishedEvent(jobStartedEvent);
		}
	}

	private void storeJobStepFinishedEvents(int jobStepCount, List<EiffelJobStepStartedEvent> jobStepStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'job step finished event'", jobStepCount, EiffelJobStepFinishedEvent.class.getName())); 
		for (EiffelJobStepStartedEvent jobStepStartedEvent : jobStepStartedEvents) {
			createAndSendJobStepFinishedEvent(jobStepStartedEvent);
		}
	}

	private void storeTestSuiteFinishedEvents(int testSuiteCount, List<EiffelTestSuiteStartedEvent> testSuiteStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'test suite finished event'", testSuiteCount, EiffelTestSuiteFinishedEvent.class.getName())); 
		for (EiffelTestSuiteStartedEvent testSuiteStarted : testSuiteStartedEvents) {
			createAndSendTestSuiteFinishedEvent(testSuiteStarted);
		}
	}

	private void storeTestCaseFinishedEvents(int testCaseCount, List<EiffelTestCaseStartedEvent> testCaseStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'test case finished event'", testCaseCount, EiffelTestCaseFinishedEvent.class.getName())); 
		for (EiffelTestCaseStartedEvent testCaseStarted : testCaseStartedEvents) {
			createAndSendTestCaseFinishedEvent(testCaseStarted);
		}
	}

	private void storeSampleEvents(int sampleCount, List<EiffelTestCaseStartedEvent> testCaseStartedEvents) {
		log.info(String.format("Will create and send %d 'sample events' of type %s for each 'test case started event'", sampleCount, TestSampleEvent.class.getName()));
		for (EiffelTestCaseStartedEvent testCaseStartedEvent : testCaseStartedEvents) {
			createAndSendSampleEvents(testCaseStartedEvent.getTestCaseExecutionId(), sampleCount);
		}
		log.info("Done");
	}

	private List<EiffelTestCaseStartedEvent> storeTestCaseStartedEvents(int testCaseCount, List<EiffelTestSuiteStartedEvent> testSuiteStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'test suite started event'", 
				testCaseCount, EiffelTestCaseStartedEvent.class.getName()));
		List<EiffelTestCaseStartedEvent> testCaseStartedEvents = Lists.newArrayList();
		for (EiffelTestSuiteStartedEvent testSuiteStartedEvent : testSuiteStartedEvents) {
			testCaseStartedEvents.addAll(createAndSendTestCaseStartedEvents(testSuiteStartedEvent.getTestSuiteExecutionId(), 
					"Test #" + System.currentTimeMillis(), "Performance test",  testCaseCount));
		}
		log.info("Done");
		return testCaseStartedEvents;
	}

	private List<EiffelTestSuiteStartedEvent> storeTestSuiteStartedEvents(int testSuiteCount, List<EiffelJobStepStartedEvent> jobStepStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'job step started event'", 
				testSuiteCount, EiffelTestSuiteStartedEvent.class.getName()));
		List<EiffelTestSuiteStartedEvent> testSuiteStartedEvents = Lists.newArrayList();
		for (EiffelJobStepStartedEvent jobStepStartedEvent : jobStepStartedEvents) {
			testSuiteStartedEvents.addAll(createAndSendTestSuiteStartedEvents(jobStepStartedEvent.getJobStepExecutionId(), 
					"Performance test type", "Performance test suite",  testSuiteCount));
		}
		log.info("Done");
		return testSuiteStartedEvents;
	}

	private List<EiffelJobStepStartedEvent> storeJobStepStartedEvents(int jobStepCount, EiffelJobStartedEvent[] jobStartedEvents) {
		log.info(String.format("Will create and send %d instances of %s for each 'job started event'", 
				jobStepCount, EiffelJobStepStartedEvent.class.getName()));
		List<EiffelJobStepStartedEvent> jobStepStartedEvents = Lists.newArrayList();
		for (EiffelJobStartedEvent jobStartedEvent : jobStartedEvents) {
			jobStepStartedEvents.addAll(createAndSendJobStepStartedEvents(jobStartedEvent.getJobExecutionId(), jobStepCount));
		}
		log.info("Done");
		return jobStepStartedEvents;
	}

	private EiffelJobStartedEvent[] createAndSendJobStartedEvents(int jobCount) {
		EiffelJobStartedEvent[] result = new EiffelJobStartedEvent[jobCount];
		for (int i=0; i<jobCount; i++) {
			EiffelJobStartedEvent event = createAndSendJobStartedEvent(i);
			result[i] = event;
		}
		return result;
	}

	private EiffelJobStartedEvent createAndSendJobStartedEvent(int eventNumber) {
		EiffelJobStartedEvent event = EiffelJobStartedEvent.Factory.create("Performance Test Job-" + (eventNumber+1), new ExecutionId(), eventNumber+1);
		sendEvent(event);
		return event;
	}

	private List<EiffelJobStepStartedEvent> createAndSendJobStepStartedEvents(ExecutionId parentExecutionId, int jobStepCount) {
		List<EiffelJobStepStartedEvent> result = Lists.newArrayList();
		for (int i=0; i<jobStepCount; i++) {
			EiffelJobStepStartedEvent event = createAndSendJobStepStartedEvent(parentExecutionId);
			result.add(event);
		}
		return result;
	}

	private EiffelJobStepStartedEvent createAndSendJobStepStartedEvent(ExecutionId parentExecutionId) {
		EiffelJobStepStartedEvent event = EiffelJobStepStartedEvent.Factory.create(parentExecutionId, EXPECTED_JOB_STEP_DURATION_IN_SECS, 
				TEST_SUITE_COUNT, new ExecutionId());
		sendEvent(event);
		return event;
	}

	private List<EiffelTestSuiteStartedEvent> createAndSendTestSuiteStartedEvents(ExecutionId jobStepExecutionId, String suiteType, String suiteName, 
			int testCaseCount) {
		List<EiffelTestSuiteStartedEvent> result = Lists.newArrayList();
		for (int i=0; i<testCaseCount; i++) {
			EiffelTestSuiteStartedEvent event = createAndSendTestSuiteStartedEvent(jobStepExecutionId, suiteType, suiteName, i);
			result.add(event);
		}
		return result;
	}

	private EiffelTestSuiteStartedEvent createAndSendTestSuiteStartedEvent(ExecutionId jobStepExecutionId, String suiteType, String suiteName, int index) {
		EiffelTestSuiteStartedEvent event = EiffelTestSuiteStartedEvent.Factory.create(jobStepExecutionId, suiteType, suiteName + "-" + (index+1), new ExecutionId());
		sendEvent(event);
		return event;
	}

	private List<EiffelTestCaseStartedEvent> createAndSendTestCaseStartedEvents(ExecutionId testSuiteExecutionId, String testId,
			String testName, int testCaseCount) {
		List<EiffelTestCaseStartedEvent> result = Lists.newArrayList();
		for (int i=0; i<testCaseCount; i++) {
			EiffelTestCaseStartedEvent event = createAndSendTestCaseStartedEvent(testSuiteExecutionId, testId, testName, i);
			result.add(event);
		}
		return result;
	}

	private EiffelTestCaseStartedEvent createAndSendTestCaseStartedEvent(ExecutionId testSuiteExecutionId, String testId, String testName, int index) {
		EiffelTestCaseStartedEvent event = EiffelTestCaseStartedEvent.Factory.create(testSuiteExecutionId, testId, testName + "-" + (index+1), null, new ExecutionId());
		sendEvent(event);
		return event;
	}

	private void createAndSendSampleEvents(ExecutionId testCaseExecutionId, int sampleCount) {
		for (int i=0; i<sampleCount; i++) {
			TestSampleEvent event = sampleEventBuilder.createTestSampleEvent(testCaseExecutionId, i+1);
			sendEvent(event);
		}
	}

	private EiffelJobFinishedEvent createAndSendJobFinishedEvent(EiffelJobStartedEvent jobStartedEvent) {
		EiffelJobFinishedEvent event = EiffelJobFinishedEvent.Factory.create(jobStartedEvent.getJobInstance(), jobStartedEvent.getJobExecutionId(), 
				jobStartedEvent.getJobExecutionNumber(), getRandomResultCode(), null);
		sendEvent(event);
		return event;
	}

	private EiffelJobStepFinishedEvent createAndSendJobStepFinishedEvent(EiffelJobStepStartedEvent jobStepStartedEvent) {
		EiffelJobStepFinishedEvent event = EiffelJobStepFinishedEvent.Factory.create(getRandomResultCode(), null, jobStepStartedEvent.getJobStepExecutionId());
		sendEvent(event);
		return event;
	}

	private EiffelTestSuiteFinishedEvent createAndSendTestSuiteFinishedEvent(EiffelTestSuiteStartedEvent testSuiteStarted) {
		EiffelTestSuiteFinishedEvent event = EiffelTestSuiteFinishedEvent.Factory.create(getRandomResultCode(), null, testSuiteStarted.getTestSuiteExecutionId());
		sendEvent(event);
		return event;
	}

	private ResultCode getRandomResultCode() {
		return EventBuilderHelper.getRandomResultCode(TEST_SUCCESS_PROBABILITY);
	}

	private EiffelTestCaseFinishedEvent createAndSendTestCaseFinishedEvent(EiffelTestCaseStartedEvent testCaseStarted) {
		EiffelTestCaseFinishedEvent event = EiffelTestCaseFinishedEvent.Factory.create(getRandomResultCode(), null, testCaseStarted.getTestCaseExecutionId());
		sendEvent(event);
		return event;
	}

	private void sendEvent(EiffelEvent event) {

		//		operator.sendMessage(event);
		//		MessageSender sender = new MessageSender.Factory(logger, new PerformanceTestEiffelConfiguration()).create();

		try {
			MessageSender sender = senderContainer.get();
			//			if (sender != null) {
			sender.send(event);
			//			}
			//			sender.dispose();
		} catch (Exception e) {
			log.error("Failed to send " + event, e);
		}
	}

}
