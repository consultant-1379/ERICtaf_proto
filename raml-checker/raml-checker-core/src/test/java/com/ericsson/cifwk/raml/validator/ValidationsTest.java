package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Check;
import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Violation;
import org.junit.Before;
import org.junit.Test;
import org.raml.model.Raml;

import java.util.Stack;

import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ValidationsTest {

    private Raml model;
    private Report report;

    private Stack<String> stack;

    @Before
    public void setUp() throws Exception {
        model = mock(Raml.class);
        report = mock(Report.class);
        stack = new Stack<>();
    }

    @Test
    public void failFast() throws Exception {
        SimpleViolation violation = error("fail");
        Check[] checks = new Check[]{
                pushToStack(stack, null),
                pushToStack(stack, violation),
                pushToStack(stack, violation),
                pushToStack(stack, violation)
        };

        Validations.failFast(model, report, checks);

        assertThat(stack).containsExactly("success", "fail");
        verify(report).accept(violation);
    }

    @Test
    public void failAll() throws Exception {
        SimpleViolation violation = error("fail");
        Check[] checks = new Check[]{
                pushToStack(stack, null),
                pushToStack(stack, violation),
                pushToStack(stack, violation),
                pushToStack(stack, violation)
        };

        Validations.failAll(model, report, checks);

        assertThat(stack).containsExactly("success", "fail", "fail", "fail");
        verify(report, times(3)).accept(violation);
    }

    private Check pushToStack(Stack<String> stack,
                              SimpleViolation violation) {
        return model -> {
            stack.push(ofNullable(violation)
                    .map(Violation::getMessage)
                    .orElse("success"));
            return ofNullable(violation);
        };
    }
}
