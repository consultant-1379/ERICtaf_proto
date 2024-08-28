package com.ericsson.cifwk.raml;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlLoaders;

public abstract class RamlTest {

    protected RamlDefinition load(String name) {
        return RamlLoaders.fromClasspath().load(name);
    }
}
