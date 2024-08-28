package com.ericsson.cifwk.raml.api;

public interface ReportPrinter {

    default void print(Report report) {
        if (report.hasViolations()) {
            failure(report);
        } else {
            success(report);
        }
    }

    void failure(Report report);

    void success(Report report);
}
