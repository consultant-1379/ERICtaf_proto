package com.ericsson.cifwk.tdm.client.services;

import com.ericsson.cifwk.tdm.client.model.DataRecord;
import com.ericsson.cifwk.tdm.client.model.Lock;
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
public interface LockService {

    @POST("locks")
    Call<Lock> createDataRecordLock(@Body Lock lock);

    @GET("locks/{lockId}/records")
    Call<List<DataRecord>> getDataRecordsForLock(@Path("lockId") String lockId);

    @DELETE("locks/{lockId}")
    Call releaseLock(@Path("lockId") String dataSourceId);

}
