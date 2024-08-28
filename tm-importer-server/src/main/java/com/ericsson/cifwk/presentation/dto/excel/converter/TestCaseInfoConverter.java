package com.ericsson.cifwk.presentation.dto.excel.converter;


import com.ericsson.cifwk.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.presentation.dto.data.References;
import com.ericsson.cifwk.presentation.dto.excel.Reader.ReaderForXLS;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by egergle on 19/02/2015.
 */


public class TestCaseInfoConverter {

    private String component = "End-To-End";
    private String testType = "Functional";
    private String testPriority = "Unknown";
    private String testPriorityIndex = "4";
    private String testCaseIdPattern = "Legacy-Test-";
    References references;

    public TestCaseInfoConverter () {
        references = new References();
    }

    public List<TestCaseInfo> convertToTestCaseInfo(List<Map> data) {

        List<TestCaseInfo> testCaseInfos = Lists.newArrayList();

        for (Map item : data) {
            TestCaseInfo testCaseInfo = new TestCaseInfo();
            List<RequirementInfo> requirementInfos = mapRequirments((List<String>) item.get(ReaderForXLS.REQUIREMENTS));

            testCaseInfo.setRequirements(requirementInfos);

            for (RequirementInfo requirementInfo : requirementInfos) {
                testCaseInfo.getRequirementIds().add(requirementInfo.getExternalId());
            }

            testCaseInfo.setDescription(item.get(ReaderForXLS.DESCRIPTION).toString());
            testCaseInfo.setTitle(item.get(ReaderForXLS.TITLE).toString());
            testCaseInfo.setTestCaseId(testCaseIdPattern + (new Random()).nextInt(20000000));
            setGroups(item, testCaseInfo);
            populateWithDefaults(testCaseInfo);

            mapTestSteps((List<String>) item.get(ReaderForXLS.STEPS), testCaseInfo);
            testCaseInfos.add(testCaseInfo);
        }

        return testCaseInfos;

    }

    private List<RequirementInfo> mapRequirments(List<String> externalIds){

        List<RequirementInfo> requirementInfos = Lists.newArrayList();

        for (String externalId : externalIds) {
            RequirementInfo requirementInfo = new RequirementInfo();
            requirementInfo.setExternalId(externalId);
            requirementInfos.add(requirementInfo);
        }

        return requirementInfos;
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
        testCaseInfo.setPriority(new ReferenceDataItem(testPriorityIndex, testPriority));
    }

    private void setGroups(Map item, TestCaseInfo testCaseInfo) {
        List<String> groups = (List<String>) item.get(ReaderForXLS.GROUP);
        if (groups != null) {
            for (String group : groups) {
                testCaseInfo.getGroups().add(new ReferenceDataItem(references.getGroup().get(group), group));
            }
        }
    }

    private void setComponent(Map item, TestCaseInfo testCaseInfo) {
        List<String> components = (List<String>) item.get(ReaderForXLS.GROUP);
        if (components != null) {
            for (String component : components) {
                testCaseInfo.getGroups().add(new ReferenceDataItem(references.getGroup().get(component), component));
            }
        }
    }

    public void setTestCaseIdPart(String testCaseIdPattern) {
        this.testCaseIdPattern = testCaseIdPattern;
    }
}
