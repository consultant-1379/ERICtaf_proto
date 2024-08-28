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

import java.util.List;

public class ReferenceData {

    private String id;
    private List<ReferenceDataItem> items;

    public ReferenceData() {
    }

    public ReferenceData(String id) {
        this.id = id;
        this.items = Lists.newArrayList();
    }

    public ReferenceData(String id, List<ReferenceDataItem> items) {
        this.id = id;
        this.items = items;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ReferenceDataItem> getItems() {
        return items;
    }

    public void setItems(List<ReferenceDataItem> items) {
        this.items = items;
    }

}
