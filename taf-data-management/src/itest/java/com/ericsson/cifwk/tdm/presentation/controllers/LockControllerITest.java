package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.Application;
import com.ericsson.cifwk.tdm.api.DataSourceExecution;
import com.ericsson.cifwk.tdm.api.Execution;
import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.presentation.controllers.client.ExecutionControllerClient;
import com.ericsson.cifwk.tdm.presentation.controllers.client.LockControllerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class LockControllerITest {

    @Autowired
    LockControllerClient lockControllerClient;
    @Autowired
    ExecutionControllerClient executionControllerClient;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DirtiesContext
    @Test
    public void shouldCreateDataRecordLock() throws Exception {
        Lock lock = createLockObject("56c5ddf29759e577fc68ab7f");
        MockHttpServletResponse response = lockControllerClient.createLock(lock);

        Lock createdLock = new ObjectMapper().readValue(response.getContentAsString(), Lock.class);

        assertThat(createdLock.id).isNotNull();
        assertThat(createdLock.dataSourceExecution.recordIds).contains("56c5ddd29759e577fc68ab75");
    }

    @DirtiesContext
    @Test(expected = AssertionError.class)
    public void shouldNotAllowLockRecordsTwice() throws Exception {
        Lock lock1 = createLockObject("56c5ddf29759e577fc68ab7f");

        Execution newExecution = createNewExecution();
        Lock lock2 = createLockObject(newExecution.id);

        MockHttpServletResponse firstLockResponse = lockControllerClient.createLock(lock1);
        MockHttpServletResponse secondLockResponse = lockControllerClient.createLock(lock2);
    }

    @DirtiesContext
    @Test
    public void shouldExpireLockRecordsAfterTimeout() throws Exception {
        Lock lock1 = createLockObject("56c5ddf29759e577fc68ab7f");

        Execution newExecution = createNewExecution();
        Lock lock2 = createLockObject(newExecution.id);

        MockHttpServletResponse firstLockResponse = lockControllerClient.createLock(lock1);
        Thread.sleep(60 * 1000);
        MockHttpServletResponse secondLockResponse = lockControllerClient.createLock(lock2);
    }

    private Execution createNewExecution() throws Exception {
        Execution execution = new Execution();
        execution.team = "New";
        MockHttpServletResponse executionResponse = executionControllerClient.createExecution(execution);
        return new ObjectMapper().readValue(executionResponse.getContentAsString(), Execution.class);
    }

    private Lock createLockObject(String executionId) {
        Lock lock = new Lock();
        lock.dataSourceExecution = new DataSourceExecution();
        lock.dataSourceExecution.executionId = executionId;
        lock.dataSourceExecution.dataSourceId = "56c5ddd29759e577fc68ab74";
        lock.dataSourceExecution.version = 1;

        lock.dataSourceExecution.predicates = Lists.newArrayList("name=Alderaan");
        lock.timeout = 5;
        return lock;
    }


    @Test
    public void shouldReturnDataRecordsForLock() throws Exception {

    }

    @Test
    public void shouldReleaseLock() throws Exception {

    }

}
