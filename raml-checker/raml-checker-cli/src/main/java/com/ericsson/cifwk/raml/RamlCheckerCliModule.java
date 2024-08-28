package com.ericsson.cifwk.raml;

import com.ericsson.cifwk.raml.api.ReportPrinter;
import com.ericsson.cifwk.raml.print.StdoutReportPrinter;

public class RamlCheckerCliModule extends RamlCheckerModule {

    @Override
    protected void configure() {
        bind(ReportPrinter.class).to(StdoutReportPrinter.class);
    }
}
