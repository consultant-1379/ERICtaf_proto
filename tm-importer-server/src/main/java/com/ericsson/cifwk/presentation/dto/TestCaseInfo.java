/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.presentation.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestCaseInfo extends SimpleTestCaseInfo {
    @Valid
    private ReferenceDataItem type;

    //    @Contexts
    private Set<ReferenceDataItem> contexts = Sets.newHashSet();

    private String component;

    private ReferenceDataItem priority;

    @Valid
    private Set<ReferenceDataItem> groups = Sets.newHashSet();


    private List<TestStepInfo> testSteps = Lists.newArrayList();


    private List<RequirementInfo> requirements;

    private String precondition;

    private String packageName;

    private ReferenceDataItem executionType;

    private ReferenceDataItem automationCandidate;

    private ReferenceDataItem testCaseStatus;


    private List<Modification> modifications;

    private String comment;

    private List<TestPlanInfo> associatedTestPlans;

    public ReferenceDataItem getType() {
        return type;
    }

    public void setType(ReferenceDataItem type) {
        this.type = type;
    }

    public Set<ReferenceDataItem> getContexts() {
        return Collections.unmodifiableSet(contexts);
    }

    public void addContexts(Collection<ReferenceDataItem> contexts) {
        this.contexts.addAll(contexts);
    }

    public void addContext(ReferenceDataItem context) {
        this.contexts.add(context);
    }

    public void clearContexts() {
        this.contexts.clear();
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<RequirementInfo> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementInfo> requirements) {
        this.requirements = requirements;
    }

    public ReferenceDataItem getPriority() {
        return priority;
    }

    public void setPriority(ReferenceDataItem priority) {
        this.priority = priority;
    }

    public List<TestStepInfo> getTestSteps() {
        return Collections.unmodifiableList(testSteps);
    }

    public void addTestStep(TestStepInfo testStep) {
        this.testSteps.add(testStep);
    }

    public void clearTestSteps() {
        this.testSteps.clear();
    }

    public void clearGroupIds() {
        this.groups.clear();
    }

    public Set<ReferenceDataItem> getGroups() {
        return groups;
    }

    public void addGroup(ReferenceDataItem group) {
        this.groups.add(group);
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ReferenceDataItem getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ReferenceDataItem executionType) {
        this.executionType = executionType;
    }

    public ReferenceDataItem getAutomationCandidate() {
        return automationCandidate;
    }

    public void setAutomationCandidate(ReferenceDataItem automationCandidate) {
        this.automationCandidate = automationCandidate;
    }

    public List<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(List<Modification> modifications) {
        this.modifications = modifications;
    }

    public ReferenceDataItem getTestCaseStatus() {
        return testCaseStatus;
    }

    public void setTestCaseStatus(ReferenceDataItem testCaseStatus) {
        this.testCaseStatus = testCaseStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TestPlanInfo> getAssociatedTestPlans() {
        return associatedTestPlans;
    }

    public void setAssociatedTestPlans(List<TestPlanInfo> associatedTestPlans) {
        this.associatedTestPlans = associatedTestPlans;
    }
}
