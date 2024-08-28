package com.ericsson.cifwk.tdm.client;

import com.ericsson.cifwk.tdm.client.services.DataSourceExecutionService;
import com.ericsson.cifwk.tdm.client.services.DataSourceService;
import com.ericsson.cifwk.tdm.client.services.ExecutionService;
import com.ericsson.cifwk.tdm.client.services.LockService;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
public class TDMClient {

    private final RestClientProvider restClientProvider;

    public TDMClient() {
        restClientProvider = new RestClientProvider();
    }

    public ExecutionService getExecutionService() {
        return restClientProvider.getRetrofit().create(ExecutionService.class);
    }

    public DataSourceService getDataSourceService() {
        return restClientProvider.getRetrofit().create(DataSourceService.class);
    }

    public DataSourceExecutionService getDataSourceExecutionService() {
        return restClientProvider.getRetrofit().create(DataSourceExecutionService.class);
    }

    public LockService getLockService() {
        return restClientProvider.getRetrofit().create(LockService.class);
    }

}
