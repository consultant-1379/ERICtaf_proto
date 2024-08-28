package com.ericsson.cifwk.raml.tree;

import org.raml.model.Action;
import org.raml.model.Raml;
import org.raml.model.Resource;

public interface RamlVisitor {

    void visit(Raml model);

    void visit(Resource resource);

    void visit(Action action);

}
