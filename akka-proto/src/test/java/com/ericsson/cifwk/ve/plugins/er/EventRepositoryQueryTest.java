package com.ericsson.cifwk.ve.plugins.er;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventRepositoryQueryTest {

    @Test
    public void testFromTopic() throws Exception {
        EventRepositoryQuery query = EventRepositoryQuery.fromTopic("a.b:c&&!d:e&&f");
        String queryString = query.build();

        assertEquals("a.b%3Dc%26d%21%3De", queryString);
    }

}
