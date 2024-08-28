package com.ericsson.cifwk.application.print;

import com.ericsson.cifwk.application.WebScraper;
import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Dependency;
import com.ericsson.cifwk.model.Stako;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static com.ericsson.cifwk.application.util.Exceptions.propagate;
import static java.lang.String.format;

public class DependencyPrinter {

    private static final String EXTENSION = ".csv";

    private static final String HYPERLINK_TEMPLATE = "\"=HYPERLINK(\"\"%s\"\", \"\"%s\"\")\"";
    private static final String SEARCH_PAGE = format(HYPERLINK_TEMPLATE, format(WebScraper.SEARCH_PAGE, "%s", 50), "%s");
    private static final String COMPONENT_PAGE = format(HYPERLINK_TEMPLATE, WebScraper.COMPONENT_PAGE + "%1$s", "%1$s");

    private static final String EMPTY_COMPONENT_CSV_ROW = new Component(
            "-", "-", "-", "-", Stako.UNKNOWN, "-"
    ).toCsvRow();

    private final String path;

    public DependencyPrinter(String path) {
        this.path = path + EXTENSION;
    }

    public void print(Map<Dependency, List<Component>> dependencyMap) {
        try (PrintWriter out = new PrintWriter(path)) {
            print(out, dependencyMap);
        } catch (IOException e) {
            throw propagate(e, "Could not print dependencies to '%s'", path);
        }
    }

    private void print(PrintWriter out, Map<Dependency, List<Component>> dependencyMap) {
        out.println("id,group,artifact,version,packaging,scope,matches,componentId,name,version,primId,stako,license");
        for (Map.Entry<Dependency, List<Component>> entry : dependencyMap.entrySet()) {
            print(out, entry.getKey(), entry.getValue());
            out.println("-,-,-,-,-,-,-,-,-,-,-,-,-");
        }
    }

    private void print(PrintWriter out, Dependency dependency, List<Component> components) {
        String dependencyCsvRow = dependencyCsvWithHyperlink(dependency);
        if (components.isEmpty()) {
            out.println(dependencyCsvRow + ",-," + EMPTY_COMPONENT_CSV_ROW);
        } else {
            for (Component component : components) {
                boolean matches = matches(dependency, component);
                String componentCsvRow = componentCsvWithHyperlink(component);
                out.println(dependencyCsvRow + "," + matches + "," + componentCsvRow);
            }
        }
    }

    String dependencyCsvWithHyperlink(Dependency dependency) {
        String csvRow = dependency.toCsvRow();
        String artifactId = dependency.getArtifactId();
        String hyperlink = format(SEARCH_PAGE, artifactId, artifactId);
        return csvRow.replace(dependency.quotes(artifactId), hyperlink);
    }

    String componentCsvWithHyperlink(Component component) {
        String csvRow = component.toCsvRow();
        String id = component.getComponentId();
        String hyperlink = format(COMPONENT_PAGE, id);
        return hyperlink + csvRow.substring(csvRow.indexOf(','));
    }

    private boolean matches(Dependency dependency, Component component) {
        String dependencyVersion = dependency.getVersion();
        String componentVersion = component.getVersion();
        return dependencyVersion.equalsIgnoreCase(componentVersion);
    }
}
