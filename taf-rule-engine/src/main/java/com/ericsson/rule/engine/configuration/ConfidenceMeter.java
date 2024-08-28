package com.ericsson.rule.engine.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfidenceMeter {

    final Map<String, Boolean> storyStatus = new ConcurrentHashMap<String, Boolean>();
    final Map<String, Map<String, Boolean>> testStatus = new ConcurrentHashMap<String, Map<String, Boolean>>();

    public void setUserStoryStatus(String storyId, boolean status) {
        storyStatus.put(storyId, status);
    }

    public void setTestCaseStatus(String storyId, String testId, boolean status) {
        Map<String, Boolean> map;
        if (testStatus.containsKey(storyId)) {
            map = testStatus.get(storyId);
        } else {
            map = new HashMap<String, Boolean>();
            testStatus.put(storyId, map);
        }

        map.put(testId, status);
    }

    public int currentValue() {
        int confidence = 0;

        int storyCount = storyCount();
        int storyShare;
        if (storyCount == 0) {
            return 100;
        } else {
            storyShare = 100 / storyCount;
        }

        for (Map.Entry<String, Boolean> entry : storyStatus.entrySet()) {
            Boolean status = entry.getValue();
            String storyId = entry.getKey();
            Map<String, Boolean> testStatus = this.testStatus.get(storyId);
            if (testStatus == null) {
                testStatus = new HashMap<String, Boolean>();
            }
            if (status) {
                confidence += rateStoryConfidence(storyShare, testStatus);
            } else {
                confidence += rateStoryConfidence(storyShare * 2, testStatus);
            }
        }

        // rounding bug
        if (confidence == 99) {
            confidence = 100;
        }

        return confidence;
    }

    private int storyCount() {
        int count = 0;
        for (Boolean status : storyStatus.values()) {
            if (status) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    private int rateStoryConfidence(int storyShare, Map<String, Boolean> testStatus) {
        int testCount = testStatus.size();
        if (testCount == 0) {
            return 0;
        }

        int passingTests = countPassingTests(testStatus);
        int testWeight = storyShare / testCount;

        return passingTests * testWeight;
    }

    private int countPassingTests(Map<String, Boolean> testStatus) {
        int result = 0;
        for (Boolean status : testStatus.values()) {
            if (status) {
                result += 1;
            }
        }
        return result;
    }

    public void forgetAboutStory(String storyId) {
        storyStatus.remove(storyId);
        testStatus.remove(storyId);
    }
}

