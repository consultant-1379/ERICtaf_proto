package com.ericsson.cifwk.raml.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SeverityTest {

    @Test
    public void comparison() throws Exception {
        assertThat(Severity.ERROR).isGreaterThan(Severity.WARNING);
        assertThat(Severity.WARNING).isLessThan(Severity.ERROR);
    }

    @Test
    public void isGreaterThanOrEqualTo() throws Exception {
        assertThat(Severity.ERROR.isGreaterThanOrEqualTo(Severity.ERROR)).isTrue();
        assertThat(Severity.ERROR.isGreaterThanOrEqualTo(Severity.WARNING)).isTrue();
        assertThat(Severity.WARNING.isGreaterThanOrEqualTo(Severity.WARNING)).isTrue();
        assertThat(Severity.WARNING.isGreaterThanOrEqualTo(Severity.ERROR)).isFalse();
    }
}
