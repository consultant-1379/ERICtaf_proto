package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.cluster.ResultReporter;
import com.ericsson.cifwk.taf.grid.engine.ThreadPoolTestEngine;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 *
 */
public class ThreadPoolTestEngineTest {

    ThreadPoolTestEngine engine;
    ResultReporter reporter;

    @Before
    public void setUp() {
        reporter = mock(ResultReporter.class);
        engine = new ThreadPoolTestEngine(reporter);
    }

    @Test
    public void shouldRepeat() {
        NodeInstruction instruction = new NodeInstruction();
        instruction.setContainer("");
        Map<String,String> attributes = Maps.newHashMap();

        instruction.setAttributes(attributes);
        instruction.setRunner("class");
        instruction.setId(UUID.randomUUID());
        TestSchedule schedule = new TestSchedule();
        schedule.setRepeatCount(3);
        instruction.setSchedule(schedule);
        attributes.put("className", "com.ericsson.cifwk.taf.grid.ThreadPoolTestEngineTest$SampleTest");
        attributes.put("methodName", "run");

        engine.exec(instruction);

        verify(reporter, times(3))
                .publish(
                        any(NodeInstruction.class),
                        eq(TestUpdate.ok("com.ericsson.cifwk.taf.grid.ThreadPoolTestEngineTest$SampleTest.run()", 100))
                );
    }

    public static class SampleTest {
        public void run() {
        }
    }

}
