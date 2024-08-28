package com.ericsson.cifwk.presentation.dto.excel.converter;


import com.ericsson.cifwk.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.presentation.dto.data.References;
import com.ericsson.cifwk.presentation.dto.excel.Reader.ReaderForXLS;
import com.ericsson.cifwk.presentation.dto.excel.Reader.SimpleReaderForXLS;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by egergle on 19/02/2015.
 */


public class SimpleTestCaseInfoConverter {

    private String testType = "Functional";
    private String testCaseIdPattern = "TOR13B_";
    References references;

    public SimpleTestCaseInfoConverter() {
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

            testCaseInfo.setDescription(item.get(SimpleReaderForXLS.DESCRIPTION).toString());
            testCaseInfo.setTitle(item.get(SimpleReaderForXLS.TITLE).toString());
            testCaseInfo.setTestCaseId((testCaseIdPattern + item.get(SimpleReaderForXLS.ID).toString() + "_" +
                    item.get(SimpleReaderForXLS.COMPONENT).toString()).replaceAll(" ", "_"));
            testCaseInfo.setComponent(item.get(SimpleReaderForXLS.COMPONENT).toString());
            setPriority(item, testCaseInfo);
            populateWithDefaults(testCaseInfo);

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

    private void populateWithDefaults(TestCaseInfo testCaseInfo) {
        testCaseInfo.setType(new ReferenceDataItem(references.getType().get(testType), testType));
    }


    private void setPriority(Map item, TestCaseInfo testCaseInfo) {
        String priority = item.get(SimpleReaderForXLS.PRIORITY).toString();
        if (priority != null) {
                testCaseInfo.setPriority(new ReferenceDataItem(references.getPriority().get(priority), priority));

        }
    }
}
