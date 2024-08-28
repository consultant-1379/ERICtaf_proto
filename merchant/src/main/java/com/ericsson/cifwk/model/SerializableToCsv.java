package com.ericsson.cifwk.model;

public interface SerializableToCsv {

    String toCsvRow();

    default String quotes(String string) {
        return "\"" + string + "\"";
    }
}
