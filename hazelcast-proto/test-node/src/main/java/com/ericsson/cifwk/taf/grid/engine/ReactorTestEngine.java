package com.ericsson.cifwk.taf.grid.engine;

import com.ericsson.cifwk.taf.grid.cluster.ResultReporter;
import com.ericsson.cifwk.taf.grid.runner.SpiRegistry;
import com.ericsson.cifwk.taf.grid.runner.TestContainer;
import com.ericsson.cifwk.taf.grid.runner.TestRunner;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestEngine;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static reactor.event.selector.Selectors.$;

/**
 *
 */
public final class ReactorTestEngine implements TestEngine {

    public static final String TEST_CASE_EVENT = "test-case";

    private final Logger logger = LoggerFactory.getLogger(ReactorTestEngine.class);

    private final AtomicInteger counter = new AtomicInteger();
    private final ResultReporter reporter;

    public ReactorTestEngine(ResultReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void exec(final NodeInstruction instruction) {
        final TestSchedule schedule = instruction.getSchedule();
        String runnerName = instruction.getRunner();
        SpiRegistry registry = new SpiRegistry();
        final TestRunner runner = registry.findRunner(runnerName);
        final TestContainer container = registry.findContainer(instruction.getContainer());
        String classpath = instruction.getClasspath();

        final Map<String, String> attributes = instruction.getAttributes();
        runner.setUp(container, classpath, attributes);

        final Reactor reactor = createReactor();
        reactor.on($(TEST_CASE_EVENT), new Consumer<Event<TestSpecification>>() {
            @Override
            public void accept(Event<TestSpecification> event) {
                TestRun testRun = new TestRun(instruction, runner, reporter);
                testRun.call();

                int value = counter.decrementAndGet();
                if (value == 0) {
                    runner.tearDown();
                }
            }
        });

        int vusers = schedule.getVusers();
        counter.addAndGet(vusers);
        for (int i = 0; i < vusers; i++) {
            reactor.notify(TEST_CASE_EVENT, Event.wrap(instruction));
        }

        reactor.getDispatcher().awaitAndShutdown();
        logger.info("Shutting down test reactor.");
    }

    private Reactor createReactor() {
        Environment env = new Environment();
        return Reactors
                .reactor()
                .env(env)
                .dispatcher(Environment.THREAD_POOL)
                .get();
    }

}
