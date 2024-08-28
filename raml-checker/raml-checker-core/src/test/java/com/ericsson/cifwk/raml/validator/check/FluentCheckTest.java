package com.ericsson.cifwk.raml.validator.check;

import com.ericsson.cifwk.raml.api.Violation;
import org.junit.Test;

import java.util.Optional;

import static com.ericsson.cifwk.raml.api.Severity.ERROR;
import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static org.assertj.core.api.Assertions.assertThat;

public class FluentCheckTest {

    @Test
    public void valid() throws Exception {
        assertThat(isAnswer(42)).isNotPresent();
    }

    @Test
    public void violated() throws Exception {
        assertThat(isAnswer(13)).hasValue(error("13 is not an answer"));
    }

    private Optional<Violation> isAnswer(Integer num) {
        return when(num).meets(num != 42, "is not an answer").then(ERROR);
    }

    private IntegerFluentCheck when(Integer number) {
        return new IntegerFluentCheck(number);
    }

    private static class IntegerFluentCheck extends FluentCheck<Integer, IntegerFluentCheck> {

        IntegerFluentCheck(Integer number) {
            super(number);
        }
    }
}
