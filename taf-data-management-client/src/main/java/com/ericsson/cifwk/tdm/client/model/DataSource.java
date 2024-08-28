package com.ericsson.cifwk.tdm.client.model;

import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class DataSource {

    public DataSourceIdentity identity;

    public PageInfo pageInfo;

    public List<DataRecord> records;

    public DataSourceIdentity getIdentity() {
        return identity;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<DataRecord> getRecords() {
        return records;
    }
}
