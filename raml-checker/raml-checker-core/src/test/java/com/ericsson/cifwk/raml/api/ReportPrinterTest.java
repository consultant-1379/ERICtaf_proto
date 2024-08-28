package com.ericsson.cifwk.raml.api;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ReportPrinterTest {

    private ReportPrinter printer = spy(SimpleReportPrinter.class);
    private Report report = mock(Report.class);

    @Test
    public void print_failure() throws Exception {
        doReturn(true).when(report).hasViolations();

        printer.print(report);

        verify(printer).failure(report);
        verify(printer, never()).success(any(Report.class));
    }

    @Test
    public void print_success() throws Exception {
        doReturn(false).when(report).hasViolations();

        printer.print(report);

        verify(printer).success(report);
        verify(printer, never()).failure(any(Report.class));
    }

    static class SimpleReportPrinter implements ReportPrinter {

        @Override
        public void failure(Report report) {
            // do nothing
        }

        @Override
        public void success(Report report) {
            // do nothing
        }
    }
}
