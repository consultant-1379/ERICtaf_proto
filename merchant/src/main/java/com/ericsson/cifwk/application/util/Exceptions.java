package com.ericsson.cifwk.application.util;

import static java.lang.String.format;

public final class Exceptions {

    private Exceptions() {
    }

    public static RuntimeException propagate(Throwable e, String message, Object... args) {
        throw new RuntimeException(format(message, args), e);
    }
}
