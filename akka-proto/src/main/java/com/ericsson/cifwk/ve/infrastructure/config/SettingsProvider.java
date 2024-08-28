package com.ericsson.cifwk.ve.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 *
 */
public final class SettingsProvider {

    public static final String SETTINGS_LOCATION = "settings.yml";
    public static final String DEFAULT_ENV = Environment.PRODUCTION;
    public static final String ENV_PROPERTY = "app.env";

    final Logger logger = LoggerFactory.getLogger(SettingsProvider.class.getName());

    Settings settings;

    public Settings loadSettings() {
        if (settings == null) {
            settings = getSettings();
        }
        return settings;
    }

    private Settings getSettings() {
        try (InputStream inputStream = getInputStream()) {
            String environment = determineEnvironment();
            logger.info("Loading settings for environment : {}", environment);
            return readYaml(environment, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + SETTINGS_LOCATION, e);
        }
    }

    private String determineEnvironment() {
        String environment = System.getProperty(ENV_PROPERTY);
        if (environment == null) {
            environment = DEFAULT_ENV;
        }
        return environment;
    }

    private Settings readYaml(String environment, InputStream inputStream) {
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) yaml.load(inputStream);
        Map<String, String> properties;
        if (result.containsKey(environment)) {
            properties = result.get(environment);
        } else {
            properties = result.get(DEFAULT_ENV);
        }

        return new Settings(properties);
    }

    private InputStream getInputStream() throws Exception {
        return this.getClass().getClassLoader().getResourceAsStream(SETTINGS_LOCATION);
    }

}
