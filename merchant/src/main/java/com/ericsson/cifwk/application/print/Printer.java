package com.ericsson.cifwk.application.print;

import com.ericsson.cifwk.model.Component;

import java.util.List;
import java.util.Map;

public interface Printer {

    void print(Map<String, List<Component>> componentMap);

    void print(String query, List<Component> components);

}
