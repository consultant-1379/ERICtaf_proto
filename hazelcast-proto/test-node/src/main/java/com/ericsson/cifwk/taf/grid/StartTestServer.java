package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.cluster.ClusterNode;
import com.ericsson.cifwk.taf.grid.cluster.ClusterNodeFactory;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 *
 */
public final class StartTestServer {

    public static void main(String[] args) throws Exception {
        Configuration configuration = loadConfiguration();
        final ClusterNode node = ClusterNodeFactory.createNode(configuration);
        node.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                node.shutdown();
            }
        });

        while (!Thread.currentThread().isInterrupted()) {
            Thread.sleep(1000);
        }
    }

    private static Configuration loadConfiguration() throws ConfigurationException {
        String env = System.getProperty("env", "default");
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration(env + ".properties"));
        return config;
    }


}
