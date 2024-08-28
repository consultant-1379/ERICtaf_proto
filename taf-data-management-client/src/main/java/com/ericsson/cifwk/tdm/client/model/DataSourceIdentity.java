package com.ericsson.cifwk.tdm.client.model;

import java.util.Date;

/**
 * Provides meta information on data source
 * - id
 * - record count
 * - name
 * - description
 * - version
 *
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class DataSourceIdentity {

    public String id;

    public int version;

    public String name;

    public String description;

    public boolean deleted;

    public String createdBy;
    public String updatedBy;

    public Date createTime;
    public Date updateTime;
}
