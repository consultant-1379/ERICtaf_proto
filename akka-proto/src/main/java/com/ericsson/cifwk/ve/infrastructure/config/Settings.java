package com.ericsson.cifwk.ve.infrastructure.config;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class  Settings {

    private final Map<String, String> map;

    public Settings(Map<String, String> properties) {
        this.map = properties;
    }

    public String getValue(String key) {
        return map.get(key);
    }

    public List<String> getList(String id) {
        String[] tokens = id.split("\\.");
        Map object = getObject(tokens);
        return (List<String>) object.get(tokens[tokens.length - 1]);
    }

    public String getString(String id) {
        String[] tokens = id.split("\\.");
        Map object = getObject(tokens);
        return (String) object.get(tokens[tokens.length - 1]);
    }

    public Boolean getBoolean(String id) {
        String[] tokens = id.split("\\.");
        Map object = getObject(tokens);
        return (Boolean) object.get(tokens[tokens.length - 1]);
    }

    public int getInteger(String id) {
        String[] tokens = id.split("\\.");
        Map object = getObject(tokens);
        return (Integer) object.get(tokens[tokens.length - 1]);
    }

    private Map getObject(String[] tokens) {
        Map value = map;
        int i = 0;
        while (i < tokens.length - 1) {
            value = (Map) value.get(tokens[i]);
            i++;
        }
        return value;
    }

}
