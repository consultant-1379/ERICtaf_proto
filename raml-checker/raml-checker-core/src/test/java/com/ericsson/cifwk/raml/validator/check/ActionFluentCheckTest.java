package com.ericsson.cifwk.raml.validator.check;

import com.ericsson.cifwk.raml.api.Violation;
import org.junit.Before;
import org.junit.Test;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Resource;
import org.raml.model.Response;
import org.raml.model.parameter.QueryParameter;

import java.util.Map;
import java.util.Optional;

import static com.ericsson.cifwk.raml.api.Severity.ERROR;
import static com.ericsson.cifwk.raml.validator.SimpleViolation.error;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class ActionFluentCheckTest {

    private Action action;
    private ActionFluentCheck check;

    @Before
    public void setUp() throws Exception {
        Resource resource = new Resource();
        resource.setParentUri("/foo");
        resource.setRelativeUri("/bar");

        action = new Action();
        action.setType(ActionType.GET);
        action.setResource(resource);

        check = new ActionFluentCheck(action);
    }

    @Test
    public void hasResponseCode_valid() throws Exception {
        actionResponses("200");

        Optional<Violation> result = check.hasResponseCode("500").then(ERROR);

        assertThat(result).isNotPresent();
    }

    @Test
    public void hasResponseCode_violated() throws Exception {
        actionResponses("500");

        Optional<Violation> result = check.hasResponseCode("500").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has response code 500"));
    }

    @Test
    public void hasQueryParam_valid() throws Exception {
        actionQueryParameters("baz");

        Optional<Violation> result = check.hasQueryParam("qux").then(ERROR);

        assertThat(result).isNotPresent();
    }

    @Test
    public void hasQueryParam_violated() throws Exception {
        actionQueryParameters("baz");

        Optional<Violation> result = check.hasQueryParam("baz").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has query parameter 'baz'"));
    }

    @Test
    public void hasOneOfQueryParams_valid() throws Exception {
        actionQueryParameters("baz");

        Optional<Violation> result = check.hasOneOfQueryParams("qux").then(ERROR);

        assertThat(result).isNotPresent();
    }

    @Test
    public void hasOneOfQueryParams_violated_singular() throws Exception {
        actionQueryParameters("baz", "qux");

        Optional<Violation> result = check.hasOneOfQueryParams("baz").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has query parameter 'baz'"));
    }

    @Test
    public void hasOneOfQueryParams_violated_plural() throws Exception {
        actionQueryParameters("baz", "qux");

        Optional<Violation> result = check.hasOneOfQueryParams("baz", "qux").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has query parameters 'baz'/'qux'"));
    }

    @Test
    public void hasNoTrait_valid() throws Exception {
        actionTraits("baz");

        Optional<Violation> result = check.hasNoTrait("baz").then(ERROR);

        assertThat(result).isNotPresent();
    }

    @Test
    public void hasNoTrait_violated() throws Exception {
        actionTraits("baz");

        Optional<Violation> result = check.hasNoTrait("qux").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) does not have trait 'qux'"));
    }

    @Test
    public void hasResponseCode_butNoTrait_valid() throws Exception {
        actionResponses("200");
        actionTraits("baz");

        assertThat(check.hasResponseCode("200").but().hasNoTrait("baz").then(ERROR)).isNotPresent();
        assertThat(check.hasResponseCode("500").but().hasNoTrait("baz").then(ERROR)).isNotPresent();
        assertThat(check.hasResponseCode("500").but().hasNoTrait("qux").then(ERROR)).isNotPresent();
    }

    @Test
    public void hasResponseCode_butNoTrait_violated() throws Exception {
        actionResponses("200");
        actionTraits("baz");

        Optional<Violation> result = check.hasResponseCode("200").but().hasNoTrait("qux").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has response code 200, but does not have trait 'qux'"));
    }

    @Test
    public void hasQueryParam_butNoTrait_valid() throws Exception {
        actionQueryParameters("baz");
        actionTraits("baz");

        assertThat(check.hasQueryParam("baz").but().hasNoTrait("baz").then(ERROR)).isNotPresent();
        assertThat(check.hasQueryParam("qux").but().hasNoTrait("baz").then(ERROR)).isNotPresent();
        assertThat(check.hasQueryParam("qux").but().hasNoTrait("qux").then(ERROR)).isNotPresent();
    }

    @Test
    public void hasQueryParam_butNoTrait_violated() throws Exception {
        actionQueryParameters("baz");
        actionTraits("baz");

        Optional<Violation> result = check.hasQueryParam("baz").but().hasNoTrait("qux").then(ERROR);

        assertThat(result).hasValue(error("action(GET /foo/bar) has query parameter 'baz', but does not have trait 'qux'"));
    }

    private void actionResponses(String... codes) {
        Map<String, Response> responses = newLinkedHashMap();
        for (String responseCode : codes) {
            responses.put(responseCode, new Response());
        }
        action.setResponses(responses);
    }

    private void actionQueryParameters(String... params) {
        Map<String, QueryParameter> queryParameters = newLinkedHashMap();
        for (String param : params) {
            queryParameters.put(param, new QueryParameter());
        }
        action.setQueryParameters(queryParameters);
    }

    private void actionTraits(String... traits) {
        action.setIs(newArrayList(traits));
    }
}
