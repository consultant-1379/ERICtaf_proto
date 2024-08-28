package com.ericsson.cifwk.raml.maven.plugin;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.ReportPrinter;
import com.ericsson.cifwk.raml.api.Severity;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.load.SpecLoadingException;
import com.ericsson.cifwk.raml.load.SpecificationLoader;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import static java.lang.String.format;

/**
 * Runs RAML Specification static validation.
 */
@Mojo(name = RamlCheckerMojo.GOAL, defaultPhase = LifecyclePhase.VALIDATE)
public class RamlCheckerMojo extends AbstractMojo implements ReportPrinter {

    static final String GOAL = "check";

    private SpecificationLoader loader;
    private Validator validator;

    /**
     * Path to the RAML specification.
     */
    @Parameter(property = "path", required = true)
    private File path;

    /**
     * Severity level on which the build should be failed.<br/>
     * Can take one of the two values: ERROR or WARNING.
     */
    @Parameter(property = "failOn", defaultValue = "ERROR")
    private Severity failOn;

    public RamlCheckerMojo() {
        Injector injector = Guice.createInjector(new RamlCheckerMojoModule());
        loader = injector.getInstance(SpecificationLoader.class);
        validator = injector.getInstance(Validator.class);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            runValidation(path.getPath());
        } catch (SpecLoadingException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (BuildFailureException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private void runValidation(String path) {
        Specification spec = loader.load(path);
        Report report = validator.validate(spec);
        print(report);
    }

    @Override
    public void failure(Report report) {
        getLog().error(format("RAML specification '%s' has violations:", report.name()));
        report.violations().forEach(violation -> {
            String message = violation.getMessage();
            Severity severity = violation.getSeverity();
            getLog().error(format("- [%s] %s", severity, message));
        });
        //noinspection ConstantConditions
        failBuild(report.severity().get());
    }

    private void failBuild(Severity severity) {
        if (severity.isGreaterThanOrEqualTo(failOn)) {
            throw new BuildFailureException();
        }
    }

    @Override
    public void success(Report report) {
        getLog().info(format("RAML specification '%s' is correct", report.name()));
    }

    @VisibleForTesting
    static class BuildFailureException extends RuntimeException {
        BuildFailureException() {
            super("There have been violations in RAML specification");
        }
    }
}
