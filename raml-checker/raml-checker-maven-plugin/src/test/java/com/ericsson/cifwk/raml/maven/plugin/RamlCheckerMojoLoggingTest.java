package com.ericsson.cifwk.raml.maven.plugin;

import com.ericsson.cifwk.raml.api.Severity;
import com.ericsson.cifwk.raml.load.SpecLoadingException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class RamlCheckerMojoLoggingTest {

    private static final String TEST_POM_DIRECTORY = "src/test/resources/pom/";

    @Rule
    public MojoRule mojoRule = new MojoRule();

    private Log log = spy(Log.class);

    @Test
    public void notFound() throws Exception {
        RamlCheckerMojo mojo = getRamlCheckMojo("not-found.xml");

        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasCauseInstanceOf(SpecLoadingException.class)
                .hasMessageEndingWith("not found");

        verifyZeroInteractions(log);
    }

    @Test
    public void notYaml() throws Exception {
        RamlCheckerMojo mojo = getRamlCheckMojo("not-yaml.xml");

        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasCauseInstanceOf(SpecLoadingException.class)
                .hasMessageEndingWith("not a YAML Document");

        verifyZeroInteractions(log);
    }

    @Test
    public void notRaml() throws Exception {
        RamlCheckerMojo mojo = getRamlCheckMojo("not-raml.xml");

        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasCauseInstanceOf(SpecLoadingException.class)
                .hasMessageEndingWith("not a RAML Specification");

        verifyZeroInteractions(log);
    }

    @Test
    public void incorrect() throws Exception {
        RamlCheckerMojo mojo = getRamlCheckMojo("incorrect.xml");

        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoFailureException.class)
                .hasCauseInstanceOf(RamlCheckerMojo.BuildFailureException.class);

        verify(log).error("RAML specification 'src\\test\\resources\\raml\\incorrect.raml' has violations:");
        verify(log).error("- [ERROR] Version is missing");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void correct() throws Exception {
        RamlCheckerMojo mojo = getRamlCheckMojo("correct.xml");

        mojo.execute();

        verify(log, only()).info("RAML specification 'src\\test\\resources\\raml\\correct.raml' is correct");
    }

    private RamlCheckerMojo getRamlCheckMojo(String path) throws Exception {
        RamlCheckerMojo mojo = (RamlCheckerMojo) mojoRule.lookupMojo(RamlCheckerMojo.GOAL, TEST_POM_DIRECTORY + path);
        mojoRule.setVariableValueToObject(mojo, "failOn", Severity.ERROR);
        mojo.setLog(log);
        return mojo;
    }
}
