package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.Application;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 18/02/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class DataSourceControllerITest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testName() throws Exception {
        URL url = Resources.getResource("datasources/planets.json");
        String content = Resources.toString(url, Charsets.UTF_8);

        MockHttpServletResponse response = mockMvc.perform(post("/api/datasources")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        DataSourceIdentity dataSourceIdentity = new ObjectMapper().readValue(response.getContentAsString(), DataSourceIdentity.class);

        assertThat(dataSourceIdentity.name, is("Star wars planets"));
    }
}
