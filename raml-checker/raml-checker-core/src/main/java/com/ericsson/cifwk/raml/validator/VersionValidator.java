package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Violation;
import org.raml.model.Raml;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.ericsson.cifwk.raml.api.Severity.ERROR;
import static com.ericsson.cifwk.raml.validator.Validations.failFast;
import static com.ericsson.cifwk.raml.validator.check.Checks.when;
import static java.util.regex.Pattern.compile;

public class VersionValidator extends SimpleValidator {

    private static final Pattern VERSION_PATTERN = compile("^v\\d+$");
    private static final Pattern BASE_URI_PATTERN = compile(".+/\\{version}/?.*");

    @Override
    protected void validateModel(Raml model, Report report) {
        failFast(model, report,
                this::versionPresent,
                this::versionWellFormatted,
                this::baseUriPresent,
                this::baseUriHasVersion
        );
    }

    private Optional<Violation> versionPresent(Raml model) {
        return when(model.getVersion()).as("Version").isMissing().then(ERROR);
    }

    private Optional<Violation> versionWellFormatted(Raml model) {
        return when(model.getVersion()).as("Version").doesNotMatch(VERSION_PATTERN).then(ERROR);
    }

    private Optional<Violation> baseUriPresent(Raml model) {
        return when(model.getBaseUri()).as("Base URI").isMissing().then(ERROR);
    }

    private Optional<Violation> baseUriHasVersion(Raml model) {
        return when(model.getBaseUri()).doesNotMatch(BASE_URI_PATTERN).then(ERROR, "Base URI has no version");
    }
}
