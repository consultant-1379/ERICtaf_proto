package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.Configurations;
import com.ericsson.cifwk.taf.grid.shared.CallableAdapter;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 *
 */
public final class HazelcastClusterClient implements ClusterClient {

    private final Logger logger = LoggerFactory.getLogger(HazelcastClusterClient.class);

    private final MetricsService metricsService;
    private final Configuration configuration;
    private HazelcastInstance hazelcast;

    HazelcastClusterClient(MetricsService metricsService, Configuration configuration) {
        this.metricsService = metricsService;
        this.configuration = configuration;
    }

    @Override
    public void connect() {
        ClientConfig clientConfig = new ClientConfig();
        String ipAddress = configuration.getString(Configurations.CLUSTER_IP);
        logger.info("Connecting to cluster on {}", ipAddress);
        clientConfig.addAddress(ipAddress);

        hazelcast = HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Override
    public String clientId() {
        return "not-relevant-for-hazelcast";
    }

    @Override
    public List<String> topology() {
        Cluster cluster = hazelcast.getCluster();
        Iterable<String> result = Iterables.transform(cluster.getMembers(), new Function<Member, String>() {
            @Override
            public String apply(Member input) {
                return input.getInetSocketAddress().toString();
            }
        });
        return Lists.newArrayList(result);
    }

    @Override
    public void submitToGrid(String runId, Map<String, NodeInstruction> tasks) {
        ITopic<TestUpdate> topic = hazelcast.getTopic(runId);
        topic.addMessageListener(new MessageListener<TestUpdate>() {
            @Override
            public void onMessage(Message<TestUpdate> message) {
                TestUpdate update = message.getMessageObject();
                metricsService.updateMetrics(update);
            }
        });

        submitTasks(tasks);
    }

    private List<Future<Object>> submitTasks(Map<String, NodeInstruction> tasks) {
        Cluster cluster = hazelcast.getCluster();
        Set<Member> members = cluster.getMembers();

        IExecutorService executorService = hazelcast.getExecutorService("default");

        List<Future<Object>> futures = Lists.newArrayList();
        for (Map.Entry<String, NodeInstruction> entry : tasks.entrySet()) {
            String memberName = entry.getKey();
            Member member = findByName(members, memberName);
            NodeInstruction instruction = entry.getValue();
            CallableAdapter adapter = new CallableAdapter(instruction);
            Future<Object> future = executorService.submitToMember(adapter, member);
            futures.add(future);
        }
        return futures;
    }

    private Member findByName(Set<Member> members, String id) {
        for (Member member : members) {
            if (member.getInetSocketAddress().toString().equals(id)) {
                return member;
            }
        }
        throw new IllegalArgumentException("Taf grid member not found : " + id);
    }

    @Override
    public void waitUntilAllTestsFinished() {
    }

    @Override
    public void disconnect() {
        if (hazelcast != null) {
            hazelcast.shutdown();
        }
    }

    @Override
    public MetricsService getMetricsService() {
        return metricsService;
    }
}
