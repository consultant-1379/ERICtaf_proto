package com.ericsson.cifwk.tdm.api;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Map;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class DataRecord {

    @MongoId
    @MongoObjectId
    public String id;

    public String dataSourceId;

    public String testIdColumnName;

    public Map<String, Object> values;
}
