package com.ericsson.cifwk.raml.tree;

import org.raml.model.Action;
import org.raml.model.Raml;
import org.raml.model.Resource;

import java.util.function.Consumer;

public class DelegatingRamlVisitor implements RamlVisitor {

    private Consumer<Raml> modelConsumer;
    private Consumer<Resource> resourceConsumer;
    private Consumer<Action> actionConsumer;

    public DelegatingRamlVisitor(Consumer<Raml> modelConsumer,
                                 Consumer<Resource> resourceConsumer,
                                 Consumer<Action> actionConsumer) {
        this.modelConsumer = modelConsumer;
        this.resourceConsumer = resourceConsumer;
        this.actionConsumer = actionConsumer;
    }

    @Override
    public void visit(Raml model) {
        modelConsumer.accept(model);
    }

    @Override
    public void visit(Resource resource) {
        resourceConsumer.accept(resource);
    }

    @Override
    public void visit(Action action) {
        actionConsumer.accept(action);
    }
}
