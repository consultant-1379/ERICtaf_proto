package com.ericsson.cifwk.tdm.client.model;

import java.util.Date;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 15/02/2016
 */
public class Lock {

    public String id;
    public int timeout;
    public Date createTime;

    public DataSourceExecution dataSourceExecution;
}


