package com.ericsson.cifwk.tdm.api;

import org.hibernate.validator.constraints.NotEmpty;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Date;
import java.util.Map;

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

    @MongoId
    @MongoObjectId
    public String id;

    public int version;

    @NotEmpty
    public String name;

    @NotEmpty
    public String description;

    public Map<String, Object> properties;

    public boolean deleted;

    public String createdBy;
    public String updatedBy;

    public Date createTime;
    public Date updateTime;
}
