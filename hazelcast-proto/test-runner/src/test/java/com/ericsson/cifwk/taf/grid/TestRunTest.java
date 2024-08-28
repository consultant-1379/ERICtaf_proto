package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.cluster.ResultReporter;
import com.ericsson.cifwk.taf.grid.engine.TestRun;
import com.ericsson.cifwk.taf.grid.runner.TestRunner;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 *
 */
public class TestRunTest {

    TestRun testRun;
    TestSchedule schedule;
    TestRunner runner;
    ResultReporter reporter;

    @Before
    public void setUp() {
        schedule = new TestSchedule();
        TestRunner runner = mock(TestRunner.class);
        ResultReporter reporter = mock(ResultReporter.class);
        NodeInstruction instruction = new NodeInstruction();
        instruction.setSchedule(schedule);
        testRun = new TestRun(instruction, runner, reporter);
    }

    @Test
    public void shouldRampUp() {
        testRun.call();
    }

}
