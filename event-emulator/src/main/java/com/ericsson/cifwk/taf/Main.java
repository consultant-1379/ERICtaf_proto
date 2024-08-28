package com.ericsson.cifwk.taf;

import com.ericsson.duraci.datawrappers.EventId;
import com.ericsson.duraci.datawrappers.LogReference;
import com.ericsson.duraci.datawrappers.ResultCode;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobFinishedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobStartedEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.*;
import com.ericsson.duraci.eiffelmessage.mmparser.clitool.EiffelConfig;

import java.util.HashMap;
import java.util.Random;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        EiffelConfig configuration = new EiffelConfig(Settings.DOMAIN, Settings.EXCHANGE, Settings.HOST_NAME);
        Sender sender = new Sender(configuration);
        sender.init();

        try {
            while (true) {
                eventCycle(sender);
            }
        } finally {
            sender.shutdown();
        }
    }

    private static void eventCycle(Sender sender) {
        String jobInstance = "TAF Jenkins Job";
        String jobExecutionId = "Functional_Tests";
        int jobExecutionNumber = 5;
        String suiteName = "DURA Visualization Suite";

        EiffelJobStartedEvent jobStartedEvent = EiffelJobStartedEvent.Factory.create(jobInstance, jobExecutionId, jobExecutionNumber);
        sender.send(jobStartedEvent);
        pause();
        EiffelJobStepStartedEvent jobStepStartedEvent = EiffelJobStepStartedEvent.Factory.create(new EventId(), jobInstance, jobExecutionId, jobExecutionNumber, 30L, 1L);
        sender.send(jobStepStartedEvent);
        pause();
        EiffelTestSuiteStartedEvent testSuiteStartedEvent = EiffelTestSuiteStartedEvent.Factory.create(jobStepStartedEvent.getEventId(), jobExecutionId, "Functional", suiteName);
        sender.send(testSuiteStartedEvent);
        pause();

        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3730", "MyTest1", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3732", "MyTest2", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3731", "MyTest3", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest4", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest5", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3732", "MyTest6", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3731", "MyTest7", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest8", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest9", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3732", "MyTest10", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest11", randomResult());
        testCycle(sender, jobExecutionId, testSuiteStartedEvent.getEventId(), "CIP-3788", "MyTest12", randomResult());

        sender.send(EiffelTestSuiteFinishedEvent.Factory.create(testSuiteStartedEvent.getEventId(), ResultCode.SUCCESS, new HashMap<String, LogReference>()));
        pause();
        sender.send(EiffelJobStepFinishedEvent.Factory.create(jobStepStartedEvent.getEventId(), ResultCode.SUCCESS, new HashMap<String, LogReference>()));
        pause();
        sender.send(EiffelJobFinishedEvent.Factory.create(jobInstance, jobExecutionId, jobExecutionNumber, ResultCode.SUCCESS));
        pause();
    }

    private static ResultCode randomResult() {
        Random random = new Random();
        boolean ok = random.nextBoolean();
        if (ok) {
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.FAILURE;
        }
    }

    private static void testCycle(Sender sender, String jobExecutionId, EventId jobStepEventId, String testId, String testName, ResultCode resultCode) {
        EiffelTestCaseStartedEvent testCaseStartedEvent = EiffelTestCaseStartedEvent.Factory.create(jobStepEventId, jobExecutionId, testId, testName, null);
        sender.send(testCaseStartedEvent);
        pause();
        sender.send(EiffelTestCaseFinishedEvent.Factory.create(testCaseStartedEvent.getEventId(), resultCode, new HashMap<String, LogReference>()));
        pause();
    }

    private static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
