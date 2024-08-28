package com.ericsson.cifwk.dashboard.data;

import com.ericsson.cifwk.dashboard.application.DashboardServiceModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static com.ericsson.cifwk.dashboard.test.ResourceUtils.getResourseAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EiffelMessageDataTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        DashboardServiceModule serviceModule = new DashboardServiceModule();
        mapper = serviceModule.provideObjectMapper();
    }

    @Test
    public void testMapping() throws Exception {
        String json = getResourseAsString("eiffel/message.json").trim();
        EiffelMessageWrapperData data = mapper.readValue(json, EiffelMessageWrapperData.class);

        assertNotNull(data);

        String serializedData = mapper.writeValueAsString(data);
        EiffelMessageWrapperData deserializedData =
                mapper.readValue(serializedData, EiffelMessageWrapperData.class);

        EiffelMessageData fromFile = data.getMessage();
        EiffelMessageData fromValue = deserializedData.getMessage();
        assertEquals(fromFile, fromValue);
    }

}
