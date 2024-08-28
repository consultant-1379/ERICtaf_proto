package com.ericsson.cifwk.raml;

import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.validator.CompositeValidator;
import com.ericsson.cifwk.raml.validator.RamlTesterValidator;
import com.ericsson.cifwk.raml.validator.TraitEnforcingValidator;
import com.ericsson.cifwk.raml.validator.VersionValidator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public abstract class RamlCheckerModule extends AbstractModule {

    @Override
    protected void configure() {
        // do nothing
    }

    @Provides
    public Validator validator() {
        return new CompositeValidator(
                new RamlTesterValidator(),
                new VersionValidator(),
                new TraitEnforcingValidator()
        );
    }
}
