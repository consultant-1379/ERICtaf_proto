package com.ericsson.cifwk.ve.plugins.er;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EventRepositoryQuery {

    private final StringBuilder builder;

    private EventRepositoryQuery() {
        builder = new StringBuilder();
    }

    public static EventRepositoryQuery create() {
        return new EventRepositoryQuery();
    }

    public static EventRepositoryQuery fromTopic(String topic) {
        EventRepositoryQuery query = new EventRepositoryQuery();
        String[] subscriptions = topic.trim().split("\\s*&&\\s*");
        for (String subscription : subscriptions) {
            String[] split = subscription.split("\\s*:\\s*", 2);
            if (split.length != 2) {
                continue;
            }
            String key = split[0];
            String value = split[1];
            if (key.charAt(0) == '!') {
                query.notEquals(key.substring(1), value);
            } else {
                query.equals(key, value);
            }
        }
        return query;
    }

    public EventRepositoryQuery equals(String key, String value) {
        return append(key, "=", value);
    }

    public EventRepositoryQuery notEquals(String key, String value) {
        return append(key, "!=", value);
    }

    public EventRepositoryQuery greaterThan(String key, String value) {
        return append(key, ">", value);
    }

    public EventRepositoryQuery lessThan(String key, String value) {
        return append(key, "<", value);
    }

    public EventRepositoryQuery greaterThanOrEqual(String key, String value) {
        return append(key, ">=", value);
    }

    public EventRepositoryQuery lessThanOrEqual(String key, String value) {
        return append(key, "<=", value);
    }

    private EventRepositoryQuery append(String key, String comparator, String value) {
        if (builder.length() > 0) {
            builder.append('&');
        }
        builder.append(key).append(comparator).append(value);
        return this;
    }

    public String build() {
        return toString();
    }

    @Override
    public String toString() {
        String query = builder.toString();
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
