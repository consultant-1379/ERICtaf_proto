package com.ericsson.cifwk.raml.validator.check;

import java.util.regex.Pattern;

import static joptsimple.internal.Strings.isNullOrEmpty;

public class StringFluentCheck extends FluentCheck<String, StringFluentCheck> {

    StringFluentCheck(String entity) {
        super(entity);
    }

    @Override
    protected String representation(String entity) {
        return isNullOrEmpty(entity) ? "Value" : inQuotes(entity);
    }

    public StringFluentCheck isMissing() {
        boolean condition = isNullOrEmpty(entity());
        return meets(condition, "is missing");
    }

    public StringFluentCheck doesNotMatch(Pattern pattern) {
        boolean condition = !pattern.matcher(entity()).find();
        return meets(condition, "does not match pattern ", inQuotes(pattern.pattern()));
    }

    private String inQuotes(String string) {
        return "'" + string + "'";
    }
}
