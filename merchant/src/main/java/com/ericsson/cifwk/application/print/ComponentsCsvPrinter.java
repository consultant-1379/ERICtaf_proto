package com.ericsson.cifwk.application.print;

import com.ericsson.cifwk.model.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static com.ericsson.cifwk.application.util.Exceptions.propagate;
import static java.util.stream.Collectors.toList;

public class ComponentsCsvPrinter implements Printer {

    private static final String EXTENSION = ".csv";

    private final String path;

    public ComponentsCsvPrinter(String path) {
        this.path = path + EXTENSION;
    }

    @Override
    public void print(Map<String, List<Component>> componentMap) {
        List<Component> components = componentMap.values().stream()
                .flatMap(List::stream)
                .collect(toList());
        print("", components);
    }

    @Override
    public void print(String query, List<Component> components) {
        try (PrintWriter out = new PrintWriter(path)) {
            print(out, components);
        } catch (IOException e) {
            throw propagate(e, "Could not print components to '%s'", path);
        }
    }

    private void print(PrintWriter out, List<Component> components) {
        out.println("componentId,name,version,primId,stako,license");
        for (Component component : components) {
            String csvRow = component.toCsvRow();
            out.println(csvRow);
        }
    }
}
