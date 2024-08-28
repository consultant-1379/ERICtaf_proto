package com.ericsson.cifwk.raml.api;

import com.ericsson.cifwk.raml.report.SimpleReport;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static com.ericsson.cifwk.raml.validator.SimpleViolation.warning;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ReportTest {

    private Report report;

    @Before
    public void setUp() throws Exception {
        Specification spec = mock(Specification.class);
        report = new SimpleReport(spec);
    }

    @Test
    public void hasViolations_false() throws Exception {
        assertThat(report.hasViolations()).isFalse();
    }

    @Test
    public void hasViolations_true() throws Exception {
        addViolations(error("error"));

        assertThat(report.hasViolations()).isTrue();
    }

    @Test
    public void severity_none() throws Exception {
        assertThat(report.severity()).isNotPresent();
    }

    @Test
    public void severity_warning() throws Exception {
        addViolations(warning("warning1"), warning("warning2"));

        assertThat(report.severity()).hasValue(Severity.WARNING);
    }

    @Test
    public void severity_error() throws Exception {
        addViolations(warning("warning"), error("error"));

        assertThat(report.severity()).hasValue(Severity.ERROR);
    }

    private void addViolations(Violation... violations) {
        Stream.of(violations).forEach(report);
    }
}