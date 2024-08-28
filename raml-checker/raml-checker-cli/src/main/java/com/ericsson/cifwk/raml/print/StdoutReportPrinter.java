package com.ericsson.cifwk.raml.print;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.ReportPrinter;
import com.ericsson.cifwk.raml.api.Severity;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class StdoutReportPrinter implements ReportPrinter {

    private static final Logger logger = getLogger(StdoutReportPrinter.class);

    @Override
    public void failure(Report report) {
        //noinspection ConstantConditions
        log(report.severity().get(), "RAML specification '{}' has violations:", report.name());
        report.violations().forEach(violation -> {
            String message = violation.getMessage();
            Severity severity = violation.getSeverity();
            log(severity, "- [{}] {}", severity, message);
        });
    }

    private void log(Severity severity, String format, Object... args) {
        switch (severity) {
            case ERROR:
                logger.error(format, args);
                break;
            case WARNING:
                logger.warn(format, args);
                break;
        }
    }

    @Override
    public void success(Report report) {
        logger.info("RAML specification '{}' is correct", report.name());
    }
}
