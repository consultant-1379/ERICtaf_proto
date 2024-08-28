package com.ericsson.cifwk.raml.load;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecLoadingExceptionTest {

    private SpecLoadingException exception;

    @Before
    public void setUp() throws Exception {
        exception = new SpecLoadingException("foo", "bar", new RuntimeException());
    }

    @Test
    public void unchecked() throws Exception {
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void message() throws Exception {
        assertThat(exception).hasMessage("Could not load 'foo': bar");
    }
}
