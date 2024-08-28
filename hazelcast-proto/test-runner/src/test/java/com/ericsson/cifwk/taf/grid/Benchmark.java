package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.client.ClusterClient;
import com.ericsson.cifwk.taf.grid.client.DistributedTestRunner;
import com.ericsson.cifwk.taf.grid.client.MetricsService;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.ericsson.cifwk.taf.grid.web.WebApplication;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import com.google.common.collect.Maps;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Benchmark {

    @Before
    public void setUp() {
        System.setProperty("grid", "jgroups");
    }

    @Test
    public void testPerformance() throws Exception {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration("default.properties"));

        WebApplication application = new WebApplication(config);
        DistributedTestRunner runner = application.startServer();

        ClusterClient clusterClient = runner.getClusterClient();
        List<String> topology = clusterClient.topology();
        TestSpecification specification = buildSpecification(topology);

        runner.run(specification);

        clusterClient.waitUntilAllTestsFinished();

        MetricsService metricsService = clusterClient.getMetricsService();
        Metrics metrics = metricsService.getMetrics();

        System.out.println(metrics.getSuccessCount() + " calls");
        System.out.println(metrics.getThroughput() + " calls/sec");
        System.out.println(metrics.getSuccessPercent() + " %");
    }

    private TestSpecification buildSpecification(List<String> topology) {
        String[] members = topology.toArray(new String[]{});
        TestSpecification specification = new TestSpecification();
        specification.setTestware("com.ericsson.cifwk.taf.grid:testware:1.0.0-SNAPSHOT");
        specification.setRunner("class");
        specification.setContainer("isolated");

        TestSchedule schedule = new TestSchedule();
        schedule.setVusers(50);
        schedule.setRepeatCount(100);
        Map<String,String> attributes = Maps.newHashMap();
        attributes.put("className", "com.ericsson.cifwk.taf.grid.sample.RestTestCase");
        attributes.put("methodName", "addition");
        TestStep testStep = new TestStep(members, schedule, attributes);
        specification.setTestSteps(new TestStep[] {testStep});
        return specification;
    }

}
