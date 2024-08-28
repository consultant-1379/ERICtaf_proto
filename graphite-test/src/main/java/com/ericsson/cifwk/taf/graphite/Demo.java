package com.ericsson.cifwk.taf.graphite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class Demo {

    private final Logger logger = LoggerFactory.getLogger(Demo.class);

    private final ScheduledExecutorService scheduler;
    private final int reportRate;
    private final int graphiteRate;
    private final String graphiteFrom;
    private final String prefix;
    private final MetricsReporter metricsReporter;
    private final GraphiteClient graphiteClient;
    private final Random random;

    @Inject
    public Demo(@Named("report.scheduler") ScheduledExecutorService scheduler,
                @Named("report.prefix") String prefix,
                @Named("report.rate") int reportRate,
                @Named("graphite.rate") int graphiteRate,
                @Named("graphite.from") String graphiteFrom,
                MetricsReporter metricsReporter,
                GraphiteClient graphiteClient,
                Random random) {
        this.scheduler = scheduler;
        this.prefix = prefix;
        this.reportRate = reportRate;
        this.graphiteRate = graphiteRate;
        this.graphiteFrom = graphiteFrom;
        this.metricsReporter = metricsReporter;
        this.graphiteClient = graphiteClient;
        this.random = random;
    }

    public void start() throws IOException {
        metricsReporter.connect();
        scheduler.scheduleAtFixedRate(new ReportRunnable(), 0, reportRate, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new GraphiteRunnable(), 0, graphiteRate, TimeUnit.SECONDS);
        logger.info("Started");
    }

    public void shutdown() {
        logger.warn("Shutting down");
        try {
            metricsReporter.close();
        } catch (IOException ignored) {
        }
        scheduler.shutdown();
    }

    private class ReportRunnable implements Runnable {
        @Override
        public void run() {
            dice(6);
            dice(12);
            dice(20);
        }

        private void dice(int sides) {
            try {
                String value = Integer.toString(random.nextInt(sides) + 1);
                long time = new Date().getTime() / 1000;
                metricsReporter.send(prefix + ".random.d" + sides, value, time);
            } catch (IOException e) {
                logger.error("Failed to send AMQP message", e);
                shutdown();
            }
        }
    }

    private class GraphiteRunnable implements Runnable {
        @Override
        public void run() {
            try {
                String raw = graphiteClient.get("raw", prefix + ".random.d*", graphiteFrom);
                logger.info("Graphite update: \n{}", raw);
            } catch (IOException e) {
                logger.error("Failed to get Graphite data", e);
                shutdown();
            }
        }
    }
}
