package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.client.TaskDistribution;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class TaskDistributionTest {

    private TaskDistribution taskDistribution;

    @Before
    public void setUp() throws Exception {
        taskDistribution = new TaskDistribution();
    }

    @Test
    public void shouldWorkWithOneVUser() {
        TestSpecification specification = buildSpecification(1, "X", "Y");
        Map<String, NodeInstruction> map = taskDistribution.distributeTasks(specification, "", new String[]{}, "");

        assertThat(map.size(), equalTo(2));
        assertThat(map.get("X").getSchedule().getVusers(), equalTo(0));
        assertThat(map.get("Y").getSchedule().getVusers(), equalTo(1));
    }

    @Test
    public void shouldSendToOneNode() {
        TestSpecification specification = buildSpecification(100, "X");

        Map<String, NodeInstruction> map = taskDistribution.distributeTasks(specification, "", new String[]{}, "");

        assertThat(map.size(), equalTo(1));
        assertThat(map.get("X").getSchedule().getVusers(), equalTo(100));
    }

    @Test
    public void shouldSendToAllByDefault() {
        TestSpecification specification = buildSpecification(100);

        Map<String, NodeInstruction> map = taskDistribution.distributeTasks(specification, "", new String[]{"A", "B"}, "");

        assertThat(map.size(), equalTo(2));
        assertThat(map.get("A").getSchedule().getVusers(), equalTo(50));
        assertThat(map.get("B").getSchedule().getVusers(), equalTo(50));
    }

        @Test
    public void shouldDistributeWorkBetweenNodes() {
        TestSpecification specification = buildSpecification(100, "X", "Y", "Z");

        Map<String, NodeInstruction> map = taskDistribution.distributeTasks(specification, "", new String[]{}, "");

        assertThat(map.size(), equalTo(3));
        assertThat(map.get("X").getSchedule().getVusers(), equalTo(33));
        assertThat(map.get("Y").getSchedule().getVusers(), equalTo(33));
        assertThat(map.get("Z").getSchedule().getVusers(), equalTo(34));
    }

    private TestSpecification buildSpecification(int vusers, String... members) {
        TestSchedule schedule = new TestSchedule(100, vusers);
        TestSpecification specification = new TestSpecification();
        TestStep step = new TestStep(members, schedule, Maps.<String,String>newHashMap());
        specification.setTestSteps(new TestStep[] {step});
        return specification;
    }
}
