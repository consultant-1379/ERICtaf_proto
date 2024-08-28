package com.ericsson.cifwk.tdm.client.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 16/02/2016
 */
public class DataSourceExecution {
    public String id;

    public String dataSourceId;
    public int version;
    public String executionId;

    public List<String> recordIds = Lists.newArrayList();
    public List<String> predicates;
}
