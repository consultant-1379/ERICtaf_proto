package com.ericsson.cifwk.ve.plugins.taf;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.TestExecution;
import com.ericsson.cifwk.ve.plugins.DashboardResponseProcessor;

import java.util.HashMap;
import java.util.Map;

public class TafEventProcessor implements DashboardResponseProcessor {

    public static final String PROCESSOR_NAME = "taf";

    private static final String JOB_STEP_FINISHED_EVENT = "EiffelJobStepFinishedEvent";
    private static final String TEST_SUITE_FINISHED_EVENT = "EiffelTestSuiteFinishedEvent";
    private static final String TEST_CASE_FINISHED_EVENT = "EiffelTestCaseFinishedEvent";

    private final Map<String, EiffelMessage> incompleteEvents;

    public TafEventProcessor() {
        incompleteEvents = new HashMap<>();
    }

    @Override
    public String getMessageType() {
        return PROCESSOR_NAME;
    }

    @Override
    public Object process(EiffelMessage message) {
        EiffelMessageService ems = Bootstrap.getInstance().getEiffelMessageService();
        switch (message.getEventType()) {
            case TEST_CASE_FINISHED_EVENT:
                EiffelMessage testCase = incompleteEvents.remove(ems.getParentId(message));
                if (testCase != null) {
                    EiffelMessage testSuite = incompleteEvents.get(ems.getParentId(testCase));
                    if (testSuite != null) {
                        EiffelMessage jobStep = incompleteEvents.get(ems.getParentId(testSuite));
                        if (jobStep != null) {
                            return new TestExecution(
                                    (String) jobStep.getEventData().get("jobExecutionId"),
                                    (String) testSuite.getEventData().get("name"),
                                    (String) testCase.getEventData().get("testId"),
                                    (String) testCase.getEventData().get("testName"),
                                    ((String) message.getEventData().get("resultCode")).toLowerCase()
                            );
                        }
                    }
                }
                break;
            case JOB_STEP_FINISHED_EVENT:
            case TEST_SUITE_FINISHED_EVENT:
                incompleteEvents.remove(ems.getParentId(message));
                break;
            default:
                String id = ems.getId(message);
                if (id != null) {
                    incompleteEvents.put(id, message);
                }
        }
        return null;
    }

}
