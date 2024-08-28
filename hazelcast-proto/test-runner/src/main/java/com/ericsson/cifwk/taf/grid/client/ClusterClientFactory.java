package com.ericsson.cifwk.taf.grid.client;

import org.apache.commons.configuration.Configuration;

/**
 *
 */
public class ClusterClientFactory {

    public static final String HAZELCAST = "hazelcast";
    public static final String JGROUPS = "jgroups";

    public static ClusterClient createClient(Configuration configuration, MetricsService metricsService) {
        String grid = configuration.getString("grid", HAZELCAST);
        if (HAZELCAST.equals(grid)) {
            return new HazelcastClusterClient(metricsService, configuration);
        } else {
            return new JGroupsClusterClient(metricsService, configuration);
        }
    }

}
