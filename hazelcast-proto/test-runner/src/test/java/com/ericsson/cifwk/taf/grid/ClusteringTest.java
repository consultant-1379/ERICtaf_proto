package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.sample.SampleTestCase;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
@RunWith(Theories.class)
public class ClusteringTest {

    public static final String GRID_ATTRIBUTE = "grid";

    @DataPoint
    public static String JGROUPS_GRID = "jgroups";
    @DataPoint
    public static String HAZELCAST_GRID = "hazelcast";

    final ClusterControl control = new ClusterControl();
    Map<String,String> test;

    @Before
    public void setUp() {
        test = new HashMap<>();
        test.put("className", SampleTestCase.class.getName());
        test.put("methodName", "test");
    }

    @After
    public void tearDown() {
        control.stop();
    }

    @Theory
    @Test(expected = RuntimeException.class)
    public void shouldFailToConnectToCluster(String grid) throws Exception {
        System.setProperty(GRID_ATTRIBUTE, grid);
        control.runClient(new TestSchedule(), test);
    }

    @Theory
    @Test
    public void shouldRunOnOneInstance(String grid) throws Exception {
        System.setProperty(GRID_ATTRIBUTE, grid);
        control.startNode();
        Metrics metrics = control.runClient(new TestSchedule(), test);
        sleep(500);
        assertThat(metrics.getSuccessCount(), equalTo(1L));
    }

    @Theory
    @Test
    public void shouldRunOnTwoInstances(String grid) throws Exception {
        System.setProperty(GRID_ATTRIBUTE, grid);
        control.startNode();
        control.startNode();
        Metrics metrics = control.runClient(new TestSchedule(1, 3), test);
        sleep(500);
        assertThat(metrics.getSuccessCount(), equalTo(3L));
    }

    @Theory
    @Test
    public void shouldLoadFromMavenClasspath(String grid) throws Exception {
        System.setProperty(GRID_ATTRIBUTE, grid);
        control.startNode();
        Metrics metrics = control.runClient(new TestSchedule(1, 2), test);
        sleep(500);
        assertThat(metrics.getSuccessCount(), equalTo(2L));
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
