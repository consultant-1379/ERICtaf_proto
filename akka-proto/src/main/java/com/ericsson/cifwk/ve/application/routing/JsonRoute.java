package com.ericsson.cifwk.ve.application.routing;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nebhale.jsonpath.InvalidJsonPathExpressionException;
import com.nebhale.jsonpath.JsonPath;

import java.util.concurrent.TimeUnit;

public class JsonRoute {

    private final Cache<String, Boolean> eventIdCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    private final JsonPath path;
    private final String value;
    private final String topic;

    private JsonRoute(JsonPath path, String value, String topic) {
        this.path = path;
        this.value = value;
        this.topic = topic;
    }

    public static JsonRoute parse(String topic) {
        int colonIndex = topic.indexOf(':');
        String pathPart;
        String value;
        if (colonIndex != -1) {
            pathPart = topic.substring(0, colonIndex);
            value = topic.substring(colonIndex + 1);
        } else {
            pathPart = topic;
            value = null;
        }
        try {
            JsonPath path = JsonPath.compile("$." + pathPart);
            return new JsonRoute(path, value, topic);
        } catch (InvalidJsonPathExpressionException e) {
            return null;
        }
    }

    public boolean matches(String json) {
        Object result = path.read(json, Object.class);
        if (result == null) {
            return false;
        } else if (value == null) {
            return true;
        } else {
            return result.toString().equals(value);
        }
    }

    public String getTopic() {
        return topic;
    }

    public void addToEventHierarchy(String eventId) {
        eventIdCache.put(eventId, true);
    }

    public boolean isInEventHierarchy(String eventId) {
        return eventId != null && eventIdCache.getIfPresent(eventId) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonRoute that = (JsonRoute) o;

        if (!path.toString().equals(that.path.toString())) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path.toString().hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

}
