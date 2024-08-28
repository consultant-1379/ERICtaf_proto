package com.ericsson.cifwk.taf.grid.cluster;

import org.apache.commons.configuration.Configuration;

/**
 *
 */
public class ClusterNodeFactory {

    public static final String HAZELCAST = "hazelcast";
    public static final String JGROUPS = "jgroups";

    public static ClusterNode createNode(Configuration configuration) {
        String grid = configuration.getString("grid", HAZELCAST);
        if (HAZELCAST.equals(grid)) {
            return new HazelcastClusterNode(configuration);
        } else {
            return new JGroupsClusterNode(configuration);
        }
    }

}
