package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.shared.GridTestResult;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import com.ericsson.cifwk.taf.performance.metric.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class MetricsService {

    private final Logger logger = LoggerFactory.getLogger(MetricsService.class);

    private final Metrics metrics = new Metrics();

    public void updateMetrics(TestUpdate update) {
        GridTestResult gridResult = update.getStatus();
        TestResult testResult = TestResult.valueOf(gridResult.toString());
        metrics.update(update.getExecutionTime(), testResult);
        logger.info("Result : " + update);
    }

    public Metrics getMetrics() {
        return metrics;
    }

}
