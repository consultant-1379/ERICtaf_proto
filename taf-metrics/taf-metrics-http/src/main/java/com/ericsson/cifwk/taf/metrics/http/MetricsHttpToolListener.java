package com.ericsson.cifwk.taf.metrics.http;

import com.ericsson.cifwk.taf.metrics.queue.AmqpClient;
import com.ericsson.cifwk.taf.metrics.queue.AmqpDumpling;
import com.ericsson.cifwk.taf.metrics.sample.Dumpling;
import com.ericsson.cifwk.taf.metrics.sample.Sample;
import com.ericsson.cifwk.taf.performance.metric.TestContext;
import com.ericsson.cifwk.taf.tools.http.impl.HttpToolListener;
import com.ericsson.cifwk.taf.tools.http.impl.RequestEvent;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class MetricsHttpToolListener implements HttpToolListener {

    private static final String UNKNOWN = "UNKNOWN";

    private final String executionId;
    private final String amqpUrl;
    private final String amqpExchange;
    private Dumpling dumpling;

    public MetricsHttpToolListener(String executionId, String amqpUrl, String amqpExchange) {
        this.executionId = executionId;
        this.amqpUrl = amqpUrl;
        this.amqpExchange = amqpExchange;
    }

    public void initialize() throws Exception {
        AmqpClient amqp = AmqpClient.create(amqpUrl, amqpExchange);
        try (Dumpling dumpling = AmqpDumpling.create(amqp)) {
            dumpling.initialize();
            this.dumpling = dumpling;
        }
    }

    @Override
    public void onRequest(RequestEvent event) {
        String suiteName = TestContext.getCurrentSuiteName();
        String testName = TestContext.getCurrentTestName();
        Preconditions.checkNotNull(suiteName, "HTTP request outside of suite run");
        Preconditions.checkNotNull(testName, "HTTP request outside of test run");

        URI target = URI.create(event.getRequestTarget());

        Sample sample = Sample.builder()
                .vuserId(UNKNOWN)
                .executionId(executionId)
                .testSuite(suiteName)
                .testCase(testName)
                .eventTime(new Date())
                .protocol(target.getScheme())
                .target(target)
                .requestType(event.getRequestType())
                .requestSize(event.getRequestSize())
                .responseTime((int) event.getResponseTimeToEntityMillis())
                .latency((int) event.getResponseTimeMillis())
                .responseSize(event.getResponseSize())
                .build();
        try {
            dumpling.write(sample);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void shutdown() throws IOException {
        if (dumpling != null) {
            dumpling.close();
        }
    }

}
