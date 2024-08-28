package com.ericsson.cifwk.raml;

import ch.qos.logback.classic.Level;
import org.junit.Test;

public class RamlCheckerCliLoggingTest extends LogbackLoggingOutputTest {

    @Test
    public void noArguments() throws Exception {
        RamlCheckerCli.main();

        verifyLogMessages(Level.INFO,
                "Usage: java -jar raml-checker PATH...",
                "Perform static validation of one or more RAML Specifications located at PATHs"
        );
    }

    @Test
    public void notFound() throws Exception {
        RamlCheckerCli.main("foo.raml");

        verifyLogMessages(Level.ERROR, "Could not load 'foo.raml': not found");
    }

    @Test
    public void notYaml() throws Exception {
        RamlCheckerCli.main("target/test-classes/not-yaml.properties");

        verifyLogMessages(Level.ERROR, "Could not load 'target/test-classes/not-yaml.properties': not a YAML Document");
    }

    @Test
    public void notRaml() throws Exception {
        RamlCheckerCli.main("target/test-classes/not-raml.yaml");

        verifyLogMessages(Level.ERROR, "Could not load 'target/test-classes/not-raml.yaml': not a RAML Specification");
    }

    @Test
    public void incorrectRaml() throws Exception {
        RamlCheckerCli.main("target/test-classes/incorrect.raml");

        verifyLogMessages(Level.ERROR,
                "RAML specification 'target/test-classes/incorrect.raml' has violations:",
                "- [ERROR] Version is missing"
        );
    }

    @Test
    public void correctRaml() throws Exception {
        RamlCheckerCli.main("target/test-classes/correct.raml");

        verifyLogMessages(Level.INFO, "RAML specification 'target/test-classes/correct.raml' is correct");
    }
}