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

import com.google.common.collect.Sets;

import java.util.Set;

public class RequirementInfo {

    private Long id;
    private String externalId;
    private String type;
    private String title;
    private String summary;

    private Set<RequirementInfo> children = Sets.newHashSet();

    private Integer testCaseCount;

    private String releaseExternalId;

    private String rootTitle;

    private Set<TestCaseInfo> testCases;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public Set<RequirementInfo> getChildren() {
        return children;
    }

    public void setChildren(Set<RequirementInfo> children) {
        this.children = children;
    }

    public Integer getTestCaseCount() {
        return testCaseCount;
    }

    public void setTestCaseCount(Integer testCaseCount) {
        this.testCaseCount = testCaseCount;
    }

    public String getReleaseExternalId() {
        return releaseExternalId;
    }

    public void setReleaseExternalId(String releaseExternalId) {
        this.releaseExternalId = releaseExternalId;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public Set<TestCaseInfo> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCaseInfo> testCases) {
        this.testCases = testCases;
    }

}
