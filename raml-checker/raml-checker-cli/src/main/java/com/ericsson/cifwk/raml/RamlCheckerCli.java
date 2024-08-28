package com.ericsson.cifwk.raml;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.ReportPrinter;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.load.SpecLoadingException;
import com.ericsson.cifwk.raml.load.SpecificationLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class RamlCheckerCli {

    private static final Logger logger = getLogger(RamlCheckerCli.class);

    @Inject
    private SpecificationLoader loader;

    @Inject
    private Validator validator;

    @Inject
    private ReportPrinter printer;

    public static void main(String... args) {
        Injector injector = Guice.createInjector(new RamlCheckerCliModule());
        RamlCheckerCli instance = injector.getInstance(RamlCheckerCli.class);
        instance.check(args);
    }

    private void check(String... paths) {
        if (validArguments(paths)) {
            tryRunningValidations(paths);
        } else {
            printUsage();
        }
    }

    private boolean validArguments(String[] paths) {
        return paths.length > 0;
    }

    private void tryRunningValidations(String[] paths) {
        try {
            runValidations(loader.load(paths));
        } catch (SpecLoadingException e) {
            logger.error(e.getMessage());
        }
    }

    private void runValidations(List<Specification> specs) {
        for (Specification spec : specs) {
            Report report = validator.validate(spec);
            printer.print(report);
        }
    }

    private void printUsage() {
        logger.info("Usage: java -jar raml-checker PATH...");
        logger.info("Perform static validation of one or more RAML Specifications located at PATHs");
    }
}
