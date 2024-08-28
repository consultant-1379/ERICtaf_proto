package com.ericsson.cifwk.tdm.presentation.controllers.client;

import com.ericsson.cifwk.tdm.api.Lock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
@Service
public class LockControllerClient {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public LockControllerClient(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        objectMapper = new ObjectMapper();
    }

    public MockHttpServletResponse createLock(Lock lock) throws Exception {
        return mockMvc.perform(post("/api/locks")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lock)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }
}
