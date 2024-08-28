package com.ericsson.cifwk.raml.validator.check;

import org.raml.model.Action;

public final class Checks {

    private Checks() {
    }

    public static StringFluentCheck when(String string) {
        return new StringFluentCheck(string);
    }

    public static ActionFluentCheck when(Action action) {
        return new ActionFluentCheck(action);
    }
}
