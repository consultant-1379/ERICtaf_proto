package com.ericsson.cifwk.application.print;

import com.ericsson.cifwk.model.Component;

import java.util.List;
import java.util.Map;

public class ComponentsConsolePrinter implements Printer {

    @Override
    public void print(Map<String, List<Component>> componentMap) {
        for (Map.Entry<String, List<Component>> entry : componentMap.entrySet()) {
            print(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void print(String query, List<Component> components) {
        if (components.isEmpty()) {
            System.out.printf("No components found for query '%s':%n", query);
        } else {
            System.out.printf("Components for query '%s':%n", query);
            components.forEach(System.out::println);
        }
    }
}
