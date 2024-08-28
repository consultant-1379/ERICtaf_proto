package com.ericsson.cifwk.raml.print;

import ch.qos.logback.classic.Level;
import com.ericsson.cifwk.raml.LogbackLoggingOutputTest;
import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.report.SimpleReport;
import com.ericsson.cifwk.raml.spec.SimpleSpecification;
import org.junit.Test;

import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static com.ericsson.cifwk.raml.validator.SimpleViolation.warning;

public class StdoutReportPrinterTest extends LogbackLoggingOutputTest {

    private StdoutReportPrinter printer = new StdoutReportPrinter();

    private Report report = new SimpleReport(new SimpleSpecification("foo", null));

    @Test
    public void print_failure_errors() throws Exception {
        report.accept(error("Violation 1"));
        report.accept(error("Violation 2"));

        printer.print(report);

        verifyLogMessages(Level.ERROR,
                "RAML specification 'foo' has violations:",
                "- [ERROR] Violation 1",
                "- [ERROR] Violation 2"
        );
    }

    @Test
    public void print_failure_warnings() throws Exception {
        report.accept(warning("Violation 1"));
        report.accept(warning("Violation 2"));

        printer.print(report);

        verifyLogMessages(Level.WARN,
                "RAML specification 'foo' has violations:",
                "- [WARNING] Violation 1",
                "- [WARNING] Violation 2"
        );
    }

    @Test
    public void print_failure_mixed() throws Exception {
        report.accept(warning("Violation 1"));
        report.accept(error("Violation 2"));

        printer.print(report);

        verifyLogMessages(
                Level.ERROR, "RAML specification 'foo' has violations:",
                Level.WARN, "- [WARNING] Violation 1",
                Level.ERROR, "- [ERROR] Violation 2"
        );
    }

    @Test
    public void print_success() throws Exception {
        printer.print(report);

        verifyLogMessages(Level.INFO, "RAML specification 'foo' is correct");
    }
}
