package com.ericsson.cifwk.taf.grid;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 *
 */
public class OsgiTest {

    public static void main(String[] args) throws Exception {
        FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        Map<String, String> config = new HashMap<>();

        Framework framework = frameworkFactory.newFramework(config);
        framework.start();

        BundleContext context = framework.getBundleContext();

        List<Bundle> bundles = new LinkedList<>();

        bundles.add(context.installBundle("wrap:mvn:com.ericsson.cifwk/taf-core/2.0.6-SNAPSHOT"));

        for (Bundle bundle : bundles) {
            bundle.start();
        }

        for (Bundle bundle : bundles) {
            ServiceReference<?>[] services = bundle.getRegisteredServices();
            if (services != null) {
                for (ServiceReference<?> service : services) {
                    System.out.println("Ref : " + service);
                }
            }
        }
    }

}
