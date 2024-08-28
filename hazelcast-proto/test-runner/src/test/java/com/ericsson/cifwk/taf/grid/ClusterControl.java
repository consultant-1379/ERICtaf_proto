package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.client.ClusterClient;
import com.ericsson.cifwk.taf.grid.client.ClusterClientFactory;
import com.ericsson.cifwk.taf.grid.client.DistributedTestRunner;
import com.ericsson.cifwk.taf.grid.client.MetricsService;
import com.ericsson.cifwk.taf.grid.cluster.ClusterNode;
import com.ericsson.cifwk.taf.grid.cluster.ClusterNodeFactory;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.commons.configuration.MapConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class ClusterControl {

    DistributedTestRunner runner;
    List<ClusterNode> nodes = new ArrayList<>();
    ClusterClient clusterClient;

    public Metrics runClient(TestSchedule schedule, Map<String,String> attributes) throws Exception {
        Map<String,Object> map = Maps.newHashMap();
        map.put(Configurations.CLUSTER_IP, "127.0.0.1");
        map.put(Configurations.HTTP_PORT, "8181");
        map.put(Configurations.MAVEN_REPO, "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/groups/public");
        MapConfiguration configuration = new MapConfiguration(map);
        MetricsService metricsService = new MetricsService();
        this.clusterClient = ClusterClientFactory.createClient(configuration, metricsService);
        runner = new DistributedTestRunner(clusterClient);
        clusterClient.connect();
        List<String> topology = clusterClient.topology();
        String[] members = topology.toArray(new String[]{});
        TestSpecification specification = new TestSpecification();
        specification.setTestware("");
        specification.setRunner("class");
        TestStep testStep = new TestStep(members, schedule, attributes);
        specification.setTestSteps(new TestStep[] {testStep});
        runner.run(specification);
        return metricsService.getMetrics();
    }

    public void startNode() {
        Map<String,Object> config = Maps.newHashMap();
        ClusterNode node = ClusterNodeFactory.createNode(new MapConfiguration(config));
        nodes.add(node);
        try {
            node.start();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void stop() {
        clusterClient.disconnect();
        for (ClusterNode node : nodes) {
            node.shutdown();
        }
        nodes.clear();
    }

}
