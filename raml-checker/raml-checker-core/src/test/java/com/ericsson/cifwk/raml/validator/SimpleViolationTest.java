package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Severity;
import com.ericsson.cifwk.raml.api.Violation;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleViolationTest {

    @Test
    public void error() throws Exception {
        Violation result = SimpleViolation.error("error");

        assertThat(result.getMessage()).isEqualTo("error");
        assertThat(result.getSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    public void warning() throws Exception {
        Violation result = SimpleViolation.warning("warning");

        assertThat(result.getMessage()).isEqualTo("warning");
        assertThat(result.getSeverity()).isEqualTo(Severity.WARNING);
    }
}
