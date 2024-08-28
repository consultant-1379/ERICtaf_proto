package com.ericsson.cifwk.presentation.dto.doc.converter;


import com.ericsson.cifwk.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.presentation.dto.data.References;
import com.ericsson.cifwk.presentation.dto.doc.Impl.TextParser;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by egergle on 19/02/2015.
 */


public class TestCaseInfoConverterForDoc {

    private String component = "E2E";
    private String testType = "Functional";
    private String testPriority = "Unknown";
    private String defaultPriorityIndex = "4";
    private String legacyRequirement = "";

    private static String testCaseIdPattern = "Legacy-Test-";

    References references;

    public TestCaseInfoConverterForDoc() throws NullPointerException {
        references = new References();
    }

    public List<TestCaseInfo> convertToTestCaseInfo(List<Map> data) throws NullPointerException {

        List<TestCaseInfo> testCaseInfos = Lists.newArrayList();
        for (Map item : data) {
            TestCaseInfo testCaseInfo = new TestCaseInfo();
            if (item.get(TextParser.DESCRIPTION) != null) {

                testCaseInfo.getRequirementIds().add(legacyRequirement);
                testCaseInfo.setDescription(item.get(TextParser.DESCRIPTION).toString());

                setPriority(item, testCaseInfo);

                if (item.get(TextParser.PRECONDITION) != null) {
                    testCaseInfo.setPrecondition(item.get(TextParser.PRECONDITION).toString());
                }
                testCaseInfo.setTitle(item.get(TextParser.TITLE).toString());
                testCaseInfo.setTestCaseId(testCaseIdPattern + (new Random()).nextInt(20000000));
                populateWithDefaults(testCaseInfo);
                //setGroups(testCaseInfo);

                mapTestSteps((List<String>) item.get(TextParser.STEPS), testCaseInfo);
                testCaseInfos.add(testCaseInfo);
            } else {
                System.out.println("------Skipped-------");
                System.out.println(item.get(TextParser.TITLE).toString());
            }
        }
        return testCaseInfos;

    }

    private List<TestStepInfo> mapTestSteps(List<String> testSteps, TestCaseInfo testCaseInfo) {

        List<TestStepInfo> testStepInfos = Lists.newArrayList();

        for (String step : testSteps) {
            TestStepInfo testStepInfo = new TestStepInfo();
            testStepInfo.setName(step);
            testCaseInfo.addTestStep(testStepInfo);
        }
        return testStepInfos;
    }

    private void populateWithDefaults(TestCaseInfo testCaseInfo) {
        testCaseInfo.setComponent(component);
        testCaseInfo.setType(new ReferenceDataItem(references.getType().get(testType), testType));
    }

    private String getIntegerFromString(String value) {
        return value.replaceAll("\\D", "");
    }

    private void setPriority(Map item, TestCaseInfo testCaseInfo) {
        if (item.get(TextParser.PRIORITY) != null) {
            String priorityIndex = getIntegerFromString(item.get(TextParser.PRIORITY).toString());
            String priorityValue = references.getPriorityInReverse().get(priorityIndex);
            testCaseInfo.setPriority(new ReferenceDataItem(priorityIndex, priorityValue));
        } else {
            testCaseInfo.setPriority(new ReferenceDataItem(defaultPriorityIndex, testPriority));
        }
    }

    private void setGroups(TestCaseInfo testCaseInfo) {
        testCaseInfo.getGroups().add(new ReferenceDataItem(references.getGroup().get("GAT"), "GAT"));
    }

    private void setComponent(TestCaseInfo testCaseInfo) {
        testCaseInfo.getGroups().add(new ReferenceDataItem(references.getGroup().get(""), ""));

    }

    public void setTestCaseIdPattern(String value) {
        testCaseIdPattern = value;
    }

    public void setLegacyRequirement(String value) {
        legacyRequirement = value;
    }
}
