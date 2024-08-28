package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.Application;
import com.ericsson.cifwk.tdm.api.Execution;
import com.ericsson.cifwk.tdm.presentation.controllers.client.ExecutionControllerClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class ExecutionControllerITest {

    @Autowired
    ExecutionControllerClient executionControllerClient;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateExecution() throws Exception {
        Execution execution = new Execution();
        execution.team = "Washington Capitals";

        MockHttpServletResponse response = executionControllerClient.createExecution(execution);

        Execution createdExecution = new ObjectMapper().readValue(response.getContentAsString(), Execution.class);

        assertThat(createdExecution.team, is("Washington Capitals"));
    }

    @Test
    public void shouldReturnAllExecutions() throws Exception {
        Execution execution = new Execution();
        execution.team = "Washington Capitals";

        executionControllerClient.createExecution(execution);

        execution.team = "Red Wings";
        executionControllerClient.createExecution(execution);


        MockHttpServletResponse response = executionControllerClient.getAllExecutions();

        List<Execution> executions = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<List<Execution>>() {});

        assertThat(executions.size(), is(3));
        Set<String> executionTeams = executions.stream().map(e -> e.team)
                .collect(Collectors.toSet());

        assertTrue(executionTeams.contains("TAF"));
        assertTrue(executionTeams.contains("Red Wings"));
        assertTrue(executionTeams.contains("Washington Capitals"));
    }

    @Test
    public void shouldFinishExecution() throws Exception {
        MockHttpServletResponse response = executionControllerClient.finishExecution("56c5ddf29759e577fc68ab7f");

        Execution execution = new ObjectMapper().readValue(response.getContentAsString(), Execution.class);

        assertThat(execution.team, is("TAF"));
        assertThat(execution.endTime, notNullValue());
    }
}
