package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import org.junit.Test;

public class VersionValidatorTest extends ValidatorTest {

    private VersionValidator validator = new VersionValidator();

    @Test
    public void version_missing() throws Exception {
        Report report = run(validator, "version/version-missing.raml");

        assertHasViolations(report, "Version is missing");
    }

    @Test
    public void version_empty() throws Exception {
        Report report = run(validator, "version/version-empty.raml");

        assertHasViolations(report, "Version is missing");
    }

    @Test
    public void version_badFormat() throws Exception {
        Report report = run(validator, "version/version-bad-format.raml");

        assertHasViolations(report, "Version does not match pattern '^v\\d+$'");
    }

    @Test
    public void baseUri_missing() throws Exception {
        Report report = run(validator, "version/baseUri-missing.raml");

        assertHasViolations(report, "Base URI is missing");
    }

    @Test
    public void baseUri_empty() throws Exception {
        Report report = run(validator, "version/baseUri-empty.raml");

        assertHasViolations(report, "Base URI is missing");
    }

    @Test
    public void baseUri_no_version() throws Exception {
        Report report = run(validator, "version/baseUri-no-version.raml");

        assertHasViolations(report, "Base URI has no version");
    }

    @Test
    public void version_valid() throws Exception {
        Report report = run(validator, "version/version-valid.raml");

        assertNoViolations(report);
    }
}