package com.ericsson.cifwk.raml.load;

import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.spec.SimpleSpecification;
import guru.nidi.loader.Loader;
import guru.nidi.loader.basic.FileLoader;
import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlLoaders;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SpecificationLoader {

    public List<Specification> load(String... paths) throws SpecLoadingException {
        return Stream.of(paths)
                .map(this::load)
                .collect(toList());
    }

    public Specification load(String path) throws SpecLoadingException {
        try {
            return doLoad(path);
        } catch (Loader.ResourceNotFoundException e) {
            throw new SpecLoadingException(path, "not found", e);
        } catch (ClassCastException e) {
            throw new SpecLoadingException(path, "not a YAML Document", e);
        } catch (RuntimeException e) {
            throw new SpecLoadingException(path, "not a RAML Specification", e);
        }
    }

    private Specification doLoad(String path) {
        File file = new File(path);
        FileLoader loader = new FileLoader(file.getParentFile());
        RamlLoaders ramlLoaders = RamlLoaders.using(loader);
        RamlDefinition definition = ramlLoaders.load(file.getName());
        return new SimpleSpecification(path, definition);
    }
}
