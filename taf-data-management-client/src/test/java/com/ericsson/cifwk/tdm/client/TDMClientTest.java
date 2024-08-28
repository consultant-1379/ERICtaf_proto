package com.ericsson.cifwk.tdm.client;

import com.ericsson.cifwk.tdm.client.model.DataRecord;
import com.ericsson.cifwk.tdm.client.model.DataSourceExecution;
import com.ericsson.cifwk.tdm.client.model.DataSourceIdentity;
import com.ericsson.cifwk.tdm.client.model.Execution;
import com.ericsson.cifwk.tdm.client.model.Lock;
import com.ericsson.cifwk.tdm.client.services.DataSourceService;
import com.ericsson.cifwk.tdm.client.services.ExecutionService;
import com.ericsson.cifwk.tdm.client.services.LockService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
public class TDMClientTest {

    private TDMClient tdmClient;

    @Before
    public void setUp() {
        tdmClient = new TDMClient();
    }

    @Test
    public void checkExecutionService() throws IOException {
        ExecutionService executionService = tdmClient.getExecutionService();

        Execution execution = new Execution();
        execution.team = "Team";
        Response<Execution> response = executionService.startExecution(execution).execute();

        assertThat(response.isSuccess()).isTrue();

        response = executionService.finishExecution(response.body().id).execute();
        assertThat(response.isSuccess()).isTrue();

        response = executionService.getExecutionById(response.body().id).execute();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.body().team).isEqualTo("Team");
    }

    @Test
    public void checkDataSourceService() throws IOException {
        DataSourceService dataSourceService = tdmClient.getDataSourceService();

        Response<List<DataSourceIdentity>> response = dataSourceService.getDataSourceIdentities().execute();
        assertThat(response.isSuccess()).isTrue();

        Optional<DataSourceIdentity> maybeDatasource = response.body().stream().filter(d -> d.name.equals("Star wars planets")).findFirst();
        assertThat(maybeDatasource.isPresent()).isTrue();

        DataSourceIdentity dataSourceIdentity = maybeDatasource.get();
        Response<List<DataRecord>> recordResponse = dataSourceService.getRecords(dataSourceIdentity.id).execute();

        assertThat(recordResponse.isSuccess()).isTrue();
        assertThat(recordResponse.body()).hasSize(10);
    }

    @Test
    public void checkLockService() throws IOException {
        LockService lockService = tdmClient.getLockService();

        Lock lock = new Lock();
        lock.dataSourceExecution = new DataSourceExecution();
        lock.dataSourceExecution.dataSourceId = "56c5ddd29759e577fc68ab74";
        lock.dataSourceExecution.executionId = "56c5ddf29759e577fc68ab7f";
        lock.dataSourceExecution.predicates = Lists.newArrayList();
        lock.dataSourceExecution.predicates.add("name=Alderaan");
        lock.timeout = 10;

        Response<Lock> response = lockService.createDataRecordLock(lock).execute();
        assertThat(response.isSuccess()).isTrue();

        Response<List<DataRecord>> dataRecordsForLock = lockService.getDataRecordsForLock(response.body().id).execute();
        assertThat(dataRecordsForLock.isSuccess()).isTrue();

        assertThat(dataRecordsForLock.body().size()).isEqualTo(1);
    }
}
