package com.ericsson.cifwk.raml.load;

import com.ericsson.cifwk.raml.api.Specification;
import guru.nidi.loader.Loader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecificationLoaderTest {

    private SpecificationLoader loader = new SpecificationLoader();

    @Test
    public void notFound() throws Exception {
        assertThatThrownBy(() -> loader.load("load/foo.raml"))
                .isInstanceOf(SpecLoadingException.class)
                .hasCauseInstanceOf(Loader.ResourceNotFoundException.class)
                .hasMessageEndingWith("not found");
    }

    @Test
    public void notYaml() throws Exception {
        assertThatThrownBy(() -> loader.load("target/test-classes/load/not-yaml.properties"))
                .isInstanceOf(SpecLoadingException.class)
                .hasCauseInstanceOf(ClassCastException.class)
                .hasMessageEndingWith("not a YAML Document");
    }

    @Test
    public void notRaml() throws Exception {
        assertThatThrownBy(() -> loader.load("target/test-classes/load/not-raml.yaml"))
                .isInstanceOf(SpecLoadingException.class)
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessageEndingWith("not a RAML Specification");
    }

    @Test
    public void raml() throws Exception {
        Specification result = loader.load("target/test-classes/load/raml.raml");

        assertThat(result.getModel().getTitle()).isEqualTo("This is a RAML Specification");
    }
}
