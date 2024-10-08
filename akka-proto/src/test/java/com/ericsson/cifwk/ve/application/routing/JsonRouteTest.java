package com.ericsson.cifwk.ve.application.routing;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonRouteTest {

    @Test
    public void shouldMatch() throws Exception {
        JsonRoute routeWithValue = JsonRoute.parse("a.b:3");

        assertTrue(routeWithValue.matches("{\"a\":{\"b\":3}}"));
        assertFalse(routeWithValue.matches("{\"a\":{\"b\":4}}"));

        JsonRoute routeWithoutValue = JsonRoute.parse("a.b");

        assertTrue(routeWithoutValue.matches("{\"a\":{\"b\":4}}"));
    }
}
