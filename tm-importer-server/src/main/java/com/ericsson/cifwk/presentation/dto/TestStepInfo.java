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

import java.util.Collections;
import java.util.List;

public class TestStepInfo {

    private Long id;

    private String name;

    private String comment;

    private String data;

    private List<VerifyStepInfo> verifies = Lists.newArrayList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VerifyStepInfo> getVerifies() {
        return Collections.unmodifiableList(verifies);
    }

    public void addVerify(VerifyStepInfo verify) {
        this.verifies.add(verify);
    }

    public void clearVerifies() {
        this.verifies.clear();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestStepInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", data='" + data + '\'' +
                ", verifies=" + verifies +
                '}';
    }
}
