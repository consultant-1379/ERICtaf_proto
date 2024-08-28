package com.ericsson.cifwk.tdm.api;

import java.util.List;

/**
 * Container for data records
 * - list of data records
 * - paging information
 * - links to next and previous pages
 * - data source id
 *
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class DataRecords {
//    public Long dataSourceId;

    public PageInfo pageInfo;
    public List<DataRecord> dataRecords;

    public String nextPage;
    public String previousPage;
}
