package com.ericsson.cifwk.tdm.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class DataSource {

    @NotNull
    public DataSourceIdentity identity;

    public PageInfo pageInfo;

    @NotEmpty
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
