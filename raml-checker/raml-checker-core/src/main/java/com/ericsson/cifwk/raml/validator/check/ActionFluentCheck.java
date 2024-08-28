package com.ericsson.cifwk.raml.validator.check;

import org.raml.model.Action;
import org.raml.model.Response;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.util.stream.Collectors.joining;

public class ActionFluentCheck extends FluentCheck<Action, ActionFluentCheck> {

    ActionFluentCheck(Action action) {
        super(action);
    }

    @Override
    protected String representation(Action action) {
        String actionType = action.getType().toString();
        String resourceUri = action.getResource().getUri();
        return "action(" + actionType + " " + resourceUri + ")";
    }

    public ActionFluentCheck hasResponseCode(String responseCode) {
        Map<String, Response> responses = entity().getResponses();
        boolean condition = responses.containsKey(responseCode);
        return meets(condition, "has response code ", responseCode);
    }

    public ActionFluentCheck hasQueryParam(String queryParam) {
        return hasOneOfQueryParams(queryParam);
    }

    public ActionFluentCheck hasOneOfQueryParams(String... queryParams) {
        Set<String> used = findUsed(queryParams);
        return meets(!used.isEmpty(), composeQueryParamsMessage(used));
    }

    public ActionFluentCheck hasNoTrait(String trait) {
        boolean condition = !entity().getIs().contains(trait);
        return meets(condition, "does not have trait '", trait, "'");
    }

    private Set<String> findUsed(String[] queryParams) {
        checkArgument(queryParams.length > 0);
        return intersection(
                entity().getQueryParameters().keySet(),
                newLinkedHashSet(newHashSet(queryParams))
        );
    }

    private String[] composeQueryParamsMessage(Set<String> usedQueryParams) {
        return new String[]{
                "has query parameter",
                usedQueryParams.size() > 1 ? "s" : "", " ",
                usedQueryParams.stream().collect(joining("'/'", "'", "'"))
        };
    }
}
