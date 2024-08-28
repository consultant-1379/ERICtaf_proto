package com.ericsson.oss.bsim.getters.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.ericsson.oss.bsim.operators.BsimOperator;

public class BsimApiGetter {

    public static String readResource(String path) throws IOException {
        ClassLoader classLoader = BsimOperator.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(path);
        return new Scanner(in).useDelimiter("\\A").next();
    }

    public static String getOsgiOperator() throws IOException {
        return readResource("scripts/BsimOsgiOperator.groovy");
    }

    public static final String CEX_CONFIG = "/opt/ericsson/nms_cex_client/bin/cex_client_application.ini";
    public static final String CEX_SCRIPT = "/opt/ericsson/bin/cex_client";
}
