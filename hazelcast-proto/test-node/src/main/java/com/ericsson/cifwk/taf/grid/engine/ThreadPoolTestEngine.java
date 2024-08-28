package com.ericsson.cifwk.taf.grid.engine;

import com.ericsson.cifwk.taf.grid.cluster.ResultReporter;
import com.ericsson.cifwk.taf.grid.runner.SpiRegistry;
import com.ericsson.cifwk.taf.grid.runner.TestContainer;
import com.ericsson.cifwk.taf.grid.runner.TestRunner;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestEngine;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 */
public final class ThreadPoolTestEngine implements TestEngine {

    private final ResultReporter reporter;

    public ThreadPoolTestEngine(ResultReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void exec(final NodeInstruction instruction) {
        String runnerName = instruction.getRunner();
        String classpath = instruction.getClasspath();
        Map<String, String> attributes = instruction.getAttributes();
        String containerName = instruction.getContainer();
        final TestSchedule schedule = instruction.getSchedule();

        SpiRegistry registry = new SpiRegistry();
        final TestRunner runner = registry.findRunner(runnerName);
        final TestContainer container = registry.findContainer(containerName);

        runner.setUp(container, classpath, attributes);

        final List<Callable<Object>> tasks = new ArrayList<>();

        long from = schedule.getFrom();

        if (from > System.currentTimeMillis()) {
            Timer timer = new Timer();
            Date activationTime = new Date(from);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startExecution(instruction, runner, tasks);
                }
            }, activationTime);
        } else {
            startExecution(instruction, runner, tasks);
        }
    }

    private void startExecution(NodeInstruction instruction, TestRunner runner, List<Callable<Object>> tasks) {
        TestSchedule schedule = instruction.getSchedule();
        int vusers = schedule.getVusers();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(vusers);
        for (int i = 0; i < vusers; i++) {
            TestRun testRun = new TestRun(instruction, runner, reporter);
            tasks.add(testRun);
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executorService.shutdown();
        runner.tearDown();
    }

}
