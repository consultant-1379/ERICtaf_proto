package com.ericsson.cifwk.raml.tree;

import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Raml;
import org.raml.model.Resource;

import java.util.Map;

public final class RamlWalker {

    private RamlWalker() {
    }

    /**
     * Traverses RAML Model tree starting with the root of the tree,
     * proceeding to the top level Resources and their Actions, and
     * finishing recursively with the Sub-Resources and their Actions
     *
     * @param model   RAML Specification model for traversal
     * @param visitor Instance of a {@link RamlVisitor}
     */
    public static void walk(Raml model, RamlVisitor visitor) {
        visitor.visit(model);
        walk(model.getResources(), visitor);
    }

    private static void walk(Map<String, Resource> resources, RamlVisitor visitor) {
        for (Resource resource : resources.values()) {
            visitor.visit(resource);
            Map<ActionType, Action> actions = resource.getActions();
            for (Action action : actions.values()) {
                visitor.visit(action);
            }
            walk(resource.getResources(), visitor);
        }
    }
}
