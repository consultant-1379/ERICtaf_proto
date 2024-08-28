package com.ericsson.rule.engine.configuration;

import com.ericsson.duraci.eiffelmessage.messages.events.testing.EiffelTestCaseFinished;
import com.ericsson.rule.engine.message.sender.Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Confidence {

    private static final ConfidenceMeter confidenceMeter = new ConfidenceMeter();

    // Called from Drools code
    public static void updateConfidence(EiffelTestCaseFinished event) {
        String suiteName = event.getOptionalParameter("suiteName");
        String testCaseId = event.getOptionalParameter("testCaseId");
        String context = event.getOptionalParameter("Context");
        updateConfidence(suiteName.split(" ")[1], testCaseId + "[" + context + "]", event.getResult());
    }

    public static void updateConfidence(String suiteId, String testId, String result) {
        confidenceMeter.setTestCaseStatus(suiteId, testId, "SUCCESS".equals(result));
        broadcastConfidence();
    }

    public static void updateJiraSatus(Map<String, Boolean> translated) {
        for (Map.Entry<String, Boolean> entry : translated.entrySet()) {
            confidenceMeter.setUserStoryStatus(entry.getKey(), entry.getValue());
        }
        broadcastConfidence();
    }

    private static void broadcastConfidence() {
        MyConfiguration conf = new MyConfiguration();
        Sender sender = new Sender(conf);
        int confidence = confidenceMeter.currentValue();
        System.out.println("sending confidence update :" + confidence);
        sender.updateConfidence(confidence);
        sender.dispose();
    }

    public static void removeStory(String storyId) {
        confidenceMeter.forgetAboutStory(storyId);
    }
}