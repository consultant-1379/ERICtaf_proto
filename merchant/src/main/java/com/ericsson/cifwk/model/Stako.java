package com.ericsson.cifwk.model;

public enum Stako {

    PREFERRED("ESW1"), GENERAL_USE("ESW2"), RESTRICTED_USE("ESW3"), BANNED("ESW4"), UNKNOWN("-");

    private String esw;

    Stako(String esw) {
        this.esw = esw;
    }

    public static Stako fromESW(String esw) {
        for (Stako stako : Stako.values()) {
            if (stako.esw.equalsIgnoreCase(esw)) {
                return stako;
            }
        }
        return UNKNOWN;
    }
}
