package com.ericsson.cifwk.application.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static com.ericsson.cifwk.application.util.Exceptions.propagate;

@SuppressWarnings("WeakerAccess")
public final class IO {

    private IO() {
    }

    public static Properties loadProperties(String path) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = loadResourceStream(path);
            properties.load(inputStream);
            inputStream.close();
            return properties;
        } catch (IOException e) {
            throw propagate(e, "Could not load property file '%s'", path);
        }
    }

    public static void printToConsole(String path) {
        try (InputStream in = loadResourceStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw propagate(e, "Could not print resource '%s' to console", path);
        }
    }

    public static InputStream loadResourceStream(String path) {
        ClassLoader classLoader = IO.class.getClassLoader();
        return classLoader.getResourceAsStream(path);
    }
}
