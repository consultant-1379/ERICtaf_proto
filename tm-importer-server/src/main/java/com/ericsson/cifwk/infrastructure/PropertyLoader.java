package com.ericsson.cifwk.infrastructure;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Created by egergle on 04/03/2015.
 */
public class PropertyLoader {

    private Properties properties;
    private String propertiesFile = "config.properties";
    private static PropertyLoader instance = null;

    private PropertyLoader() {
        properties = new Properties();
        URL resource = Resources.getResource(propertiesFile);
        try {
            Reader reader = Resources.asCharSource(resource, Charsets.UTF_8).openStream();
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PropertyLoader getProperties() {
        if (instance == null) {
            return instance = new PropertyLoader();
        } else {
            return instance;
        }

    }

    public static synchronized Object getProperty(Object value) {
        return instance.properties.get(value);
    }
}
