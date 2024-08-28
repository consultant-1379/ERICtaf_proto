package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.sample.SampleTestCase;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PerformanceTest {

    final ClusterControl control = new ClusterControl();

    @After
    public void tearDown() {
        control.stop();
    }

    @Test
    public void benchmark() throws Exception {
        control.startNode();
        control.startNode();
        control.startNode();

        Map<String,String> test = new HashMap<>();
        test.put("className", SampleTestCase.class.getName());
        test.put("methodName", "test");

        TestSchedule schedule = new TestSchedule(3, Long.MIN_VALUE, Long.MAX_VALUE, 10);

        Metrics metrics = control.runClient(schedule, test);

        Thread.sleep(5000);

        System.out.println("Throughput : " + metrics.getThroughput());
        System.out.println("Success # : " + metrics.getSuccessCount());
        System.out.println("Success % : " + metrics.getSuccessPercent());
    }

}
