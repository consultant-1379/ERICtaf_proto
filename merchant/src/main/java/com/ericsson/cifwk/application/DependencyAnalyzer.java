package com.ericsson.cifwk.application;

import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Dependency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ericsson.cifwk.application.util.Exceptions.propagate;
import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;

public class DependencyAnalyzer {

    /**
     * Examples:
     * [INFO] ---
     * [INFO] +-
     * [INFO] |  \-
     */
    private static final String PREFIX_REGEX = "(?:\\[INFO\\] )?[\\s\\+\\-\\|\\\\]*";

    /**
     * Pattern:
     * groupId:artifactId:packaging:version:scope
     */
    private static final String ARTIFACT_REGEX = "(.+?):(.+?):([a-z]+?):(.+?):([a-z]+)";

    private static final Pattern DEPENDENCY_PATTERN = compile(PREFIX_REGEX + ARTIFACT_REGEX);

    private WebScraper scraper;

    public DependencyAnalyzer(WebScraper scraper) {
        this.scraper = scraper;
    }

    public Map<Dependency, List<Component>> analyze(Path path) {
        List<Dependency> dependencies = get3ppProductionDependencies(path);
        String[] queries = convertDependenciesToQueries(dependencies);
        Map<String, List<Component>> results = scraper.search(queries);
        return groupComponentsByDependencies(dependencies, results);
    }

    private List<Dependency> get3ppProductionDependencies(Path path) {
        return loadDependencies(path).stream().distinct()
                .filter(Dependency::isProductionScope)
                .filter(Dependency::is3pp)
                .collect(toList());
    }

    private List<Dependency> loadDependencies(Path path) {
        try {
            return Files.lines(path)
                    .map(this::toDependency)
                    .filter(Objects::nonNull)
                    .collect(toList());
        } catch (IOException e) {
            throw propagate(e, "Could not read path '%s'", path);
        }
    }

    private Dependency toDependency(String line) {
        Matcher matcher = DEPENDENCY_PATTERN.matcher(line);
        if (matcher.find()) {
            String groupId = matcher.group(1);
            String artifactId = matcher.group(2);
            String packaging = matcher.group(3);
            String version = matcher.group(4);
            String scope = matcher.group(5);
            return new Dependency(groupId, artifactId, version, packaging, scope);
        } else {
            return null;
        }
    }

    private String[] convertDependenciesToQueries(List<Dependency> dependencies) {
        return dependencies.stream()
                .map(Dependency::getArtifactId)
                .distinct()
                .toArray(String[]::new);
    }

    private Map<Dependency, List<Component>> groupComponentsByDependencies(List<Dependency> dependencies,
                                                                           Map<String, List<Component>> results) {
        Map<Dependency, List<Component>> dependenciesToComponents = new LinkedHashMap<>();
        for (Dependency dependency : dependencies) {
            String artifactId = dependency.getArtifactId();
            List<Component> components = results.get(artifactId);
            components = (components == null) ? emptyList() : components;
            dependenciesToComponents.put(dependency, components);
        }
        return dependenciesToComponents;
    }
}
