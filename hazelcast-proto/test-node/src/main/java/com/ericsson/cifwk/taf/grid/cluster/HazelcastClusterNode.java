package com.ericsson.cifwk.taf.grid.cluster;

import com.ericsson.cifwk.taf.grid.NodeSettings;
import com.ericsson.cifwk.taf.grid.engine.ThreadPoolTestEngine;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestEngine;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.apache.commons.configuration.Configuration;

import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public final class HazelcastClusterNode implements ClusterNode, ResultReporter {

    private final Configuration configuration;
    private HazelcastInstance hazelcast;

    HazelcastClusterNode(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start() {
        Config config = buildConfig(configuration);
        start(config);
    }

    private Config buildConfig(Configuration configuration) {
        Config config = new Config();
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(5701);
        networkConfig.setPortAutoIncrement(true);
        JoinConfig joinConfig = new JoinConfig();
        TcpIpConfig tcpIpConfig = new TcpIpConfig();
        String[] members = configuration.getString(NodeSettings.CLUSTER_MEMEBERS, "127.0.0.1").split(";");
        for (String member : members) {
            tcpIpConfig.addMember(member);
        }
        joinConfig.setTcpIpConfig(tcpIpConfig);
        networkConfig.setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);
        return config;
    }

    private void start(Config config) {
        hazelcast = Hazelcast.newHazelcastInstance(config);
        ConcurrentMap<String,Object> userContext = hazelcast.getUserContext();
        //TestEngine testEngine = new ReactorTestEngine(hazelcast);
        TestEngine testEngine = new ThreadPoolTestEngine(this);
        userContext.put(TestEngine.class.getName(), testEngine);
    }

    @Override
    public void publish(NodeInstruction instruction, TestUpdate update) {
        final String topicName = instruction.getId().toString();
        ITopic<Object> topic = hazelcast.getTopic(topicName);
        topic.publish(update);
    }

    @Override
    public void shutdown() {
        hazelcast.shutdown();
    }
}
