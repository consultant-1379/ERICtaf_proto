package com.ericsson.oss.cite.operators;

public interface EnvironmentSetUpOperator {

    boolean executeInitialInstall();

    boolean executeArneImport();

    boolean prepareUsers();
}
