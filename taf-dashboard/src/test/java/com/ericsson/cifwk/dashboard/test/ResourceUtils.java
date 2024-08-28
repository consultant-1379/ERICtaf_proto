package com.ericsson.cifwk.dashboard.test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public final class ResourceUtils {

    private ResourceUtils() {
    }

    public static String getResourseAsString(String resourceName) throws IOException {
        URL resource = Resources.getResource(resourceName);
        return Resources.toString(resource, Charsets.UTF_8);
    }

}
