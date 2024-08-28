package com.ericsson.cifwk.tdm.client.services;

import com.ericsson.cifwk.tdm.client.model.DataRecord;
import com.ericsson.cifwk.tdm.client.model.DataSource;
import com.ericsson.cifwk.tdm.client.model.DataSourceIdentity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 *         <p>
 */
public interface DataSourceService {

    @GET("datasources")
    Call<List<DataSourceIdentity>> getDataSourceIdentities();

    @POST("datasources")
    Call<DataSourceIdentity> createDataSource(@Body DataSource execution);

    @GET("datasources/{datasourceId}")
    Call<DataSourceIdentity> getDataSourceById(@Path("datasourceId") String datasourceId);

    @DELETE("datasources/{datasourceId}")
    Call<DataSourceIdentity> deleteDataSource(@Path("datasourceId") String datasourceId);

    @GET("datasources/{datasourceId}/records")
    Call<List<DataRecord>> getRecords(@Path("datasourceId") String datasourceId);

}
