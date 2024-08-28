package com.ericsson.cifwk.taf.grid.engine;

import com.ericsson.cifwk.taf.grid.AssertionException;
import com.ericsson.cifwk.taf.grid.cluster.ResultReporter;
import com.ericsson.cifwk.taf.grid.runner.TestRunner;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 *
 */
public final class TestRun implements Callable<Object> {

    private final Logger logger = LoggerFactory.getLogger(TestRun.class);

    private NodeInstruction instruction;
    private TestRunner runner;
    private ResultReporter reporter;

    public TestRun(NodeInstruction instruction, TestRunner runner, ResultReporter reporter) {
        this.instruction = instruction;
        this.runner = runner;
        this.reporter = reporter;
    }

    @Override
    public Object call() {
        runTest();
        return "OK";
    }

    private void runTest() {
        TestSchedule schedule = instruction.getSchedule();
        long until = schedule.getUntil();
        long thinkTime = schedule.getThinkTime();

        for (int i = 0; i < schedule.getRepeatCount(); i++) {
            runOnce();
            sleep(thinkTime);

            if (System.currentTimeMillis() >= until) {
                break;
            }
        }
    }

    private void runOnce() {
        String testName = runner.getTestName();
        try {
            long start = System.nanoTime();
            runner.runTest();
            long stop = System.nanoTime();
            long elapsed = (stop - start) / 1000;
            reporter.publish(instruction, TestUpdate.ok(testName, elapsed));
        } catch (AssertionException e) {
            reporter.publish(instruction, TestUpdate.fail(testName, Arrays.toString(e.getStackTrace())));
        } catch (Exception e) {
            logger.warn("Exception while running test : ", e);
            reporter.publish(instruction, TestUpdate.error(testName, Arrays.toString(e.getStackTrace())));
        }
    }

    private void sleep(long thinkTime) {
        try {
            Thread.sleep(thinkTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
