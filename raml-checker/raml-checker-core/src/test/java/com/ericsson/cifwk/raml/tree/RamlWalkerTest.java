package com.ericsson.cifwk.raml.tree;

import com.ericsson.cifwk.raml.RamlTest;
import guru.nidi.ramltester.RamlDefinition;
import org.junit.Test;

import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

public class RamlWalkerTest extends RamlTest {

    @Test
    public void traversalOrder() throws Exception {
        Stack<String> stack = new Stack<>();
        RamlDefinition definition = load("walker.raml");
        RamlVisitor visitor = new DelegatingRamlVisitor(
                model -> stack.push(model.getTitle()),
                resource -> stack.push(resource.getDisplayName()),
                action -> stack.push(action.getDisplayName())
        );

        RamlWalker.walk(definition.getRaml(), visitor);

        assertThat(stack).containsExactly(
                "Model",
                "Resource A", "Resource A GET", "Resource A POST",
                "Sub-resource AB", "Sub-resource AB GET",
                "Sub-resource AC", "Sub-resource AC GET",
                "Resource D", "Resource D GET", "Resource D POST"
        );
    }
}