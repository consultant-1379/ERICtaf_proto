package com.ericsson.cifwk.raml.validator.check;

import com.ericsson.cifwk.raml.api.Violation;
import org.junit.Test;

import java.util.Optional;

import static com.ericsson.cifwk.raml.api.Severity.ERROR;
import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static com.ericsson.cifwk.raml.validator.check.Checks.when;
import static java.util.regex.Pattern.compile;
import static org.assertj.core.api.Assertions.assertThat;

public class StringFluentCheckTest {

    private static final String MESSAGE = "message";

    @Test
    public void isMissing_null() throws Exception {
        Optional<Violation> result = when((String) null).isMissing().then(ERROR, MESSAGE);

        assertThat(result).hasValue(error(MESSAGE));
    }

    @Test
    public void isMissing_empty() throws Exception {
        Optional<Violation> result = when("").isMissing().then(ERROR, MESSAGE);

        assertThat(result).hasValue(error(MESSAGE));
    }

    @Test
    public void isMissing_notEmpty() throws Exception {
        Optional<Violation> result = when("foo").isMissing().then(ERROR, MESSAGE);

        assertThat(result).isNotPresent();
    }

    @Test
    public void isMissing_null_message() throws Exception {
        Optional<Violation> result = when((String) null).isMissing().then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("Value is missing")
        );
    }

    @Test
    public void isMissing_empty_message() throws Exception {
        Optional<Violation> result = when("").isMissing().then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("Value is missing")
        );
    }

    @Test
    public void isMissing_null_messageWithDescription() throws Exception {
        Optional<Violation> result = when((String) null).as("Foo").isMissing().then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("Foo is missing")
        );
    }

    @Test
    public void isMissing_empty_messageWithDescription() throws Exception {
        Optional<Violation> result = when("").as("Foo").isMissing().then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("Foo is missing")
        );
    }

    @Test
    public void doesNotMatch_false() throws Exception {
        Optional<Violation> result = when("foo").doesNotMatch(compile("bar")).then(ERROR, MESSAGE);

        assertThat(result).hasValue(error(MESSAGE));
    }

    @Test
    public void doesNotMatch_true() throws Exception {
        Optional<Violation> result = when("foo").doesNotMatch(compile("foo")).then(ERROR, MESSAGE);

        assertThat(result).isNotPresent();
    }

    @Test
    public void doesNotMatch_message() throws Exception {
        Optional<Violation> result = when("foo").doesNotMatch(compile("bar")).then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("'foo' does not match pattern 'bar'")
        );
    }

    @Test
    public void doesNotMatch_messageWithDescription() throws Exception {
        Optional<Violation> result = when("foo").as("Foo").doesNotMatch(compile("bar")).then(ERROR);

        assertThat(result).hasValueSatisfying(violation ->
                assertThat(violation.getMessage()).isEqualTo("Foo does not match pattern 'bar'")
        );
    }
}
