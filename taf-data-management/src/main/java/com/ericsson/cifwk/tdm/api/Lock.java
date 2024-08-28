package com.ericsson.cifwk.tdm.api;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 15/02/2016
 */
public class Lock {

    @MongoId
    @MongoObjectId
    public String id;
    @NotNull
    public int timeout;
    public Date createTime;

    public DataSourceExecution dataSourceExecution;
}


