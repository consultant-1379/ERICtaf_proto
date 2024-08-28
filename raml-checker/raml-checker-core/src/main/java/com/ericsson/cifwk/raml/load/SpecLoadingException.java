package com.ericsson.cifwk.raml.load;

import static java.lang.String.format;

public class SpecLoadingException extends RuntimeException {

    private String path;

    public SpecLoadingException(String path, String message, Throwable cause) {
        super(format("Could not load '%s': %s", path, message), cause);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
