package com.ericsson.cifwk.application;

import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Stako;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class ComponentParser {

    private static final Logger logger = LoggerFactory.getLogger(ComponentParser.class);

    private static final Pattern ID_PATTERN = compile(".*componentid=(\\d+)&?.*");
    private static final Pattern SEARCH_ENTRY_PATTERN = compile("^(.+?) Ver: (.+?) PRIM Id: (.+?) ?(?:STAKO: (ESW\\d) .+?(?:License: (.+))?)?$");
    private static final Pattern COMPONENT_PAGE_PATTERN = compile("^(.+?) General information .* PRIM Id: (.+?) .* Version: (.+?) .* License:.*\\((.*)\\).*STAKO Classification:.*(ESW.).*$");

    Optional<Component> parseSearchPageEntry(String componentId, String text) {
        Matcher matcher = SEARCH_ENTRY_PATTERN.matcher(text);
        if (matcher.find()) {
            String name = matcher.group(1);
            String version = matcher.group(2);
            String primId = matcher.group(3);
            Stako stako = Stako.fromESW(nullToNA(matcher.group(4)));
            String license = nullToNA(matcher.group(5));
            Component result = new Component(componentId, name, version, primId, stako, license);
            return Optional.of(result);
        } else {
            logger.warn("Could not parse Component from search page entry '{}'", text);
            return Optional.empty();
        }
    }

    Optional<Component> parseComponentPage(String componentId, String text) {
        Matcher matcher = COMPONENT_PAGE_PATTERN.matcher(text);
        if (matcher.find()) {
            String name = matcher.group(1);
            String primId = matcher.group(2);
            String version = matcher.group(3);
            String license = matcher.group(4).trim();
            Stako stako = Stako.fromESW(matcher.group(5));
            Component component = new Component(componentId, name, version, primId, stako, license);
            return Optional.of(component);
        } else {
            logger.warn("Could not parse Component from view page for component ID '{}'", componentId);
            return Optional.empty();
        }
    }

    private String nullToNA(String value) {
        return (value == null) ? "n/a" : value;
    }

    String parseComponentId(String attribute) {
        Matcher matcher = ID_PATTERN.matcher(attribute);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            logger.warn("Could not parse Component ID from HTML attribute '{}'", attribute);
            return "n/a";
        }
    }
}
