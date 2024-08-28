package com.ericsson.cifwk.tdm.api;

import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotEmpty;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 16/02/2016
 */
public class DataSourceExecution {
    @MongoId
    @MongoObjectId
    public String id;

    @NotEmpty
    public String dataSourceId;
    @NotNull
    public int version;
    @NotEmpty
    public String executionId;

    public String flowOrTestStep;

    public List<String> recordIds = Lists.newArrayList();
    public List<String> predicates;
}
