package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.client.MetricsService;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import org.junit.Test;

public class MetricsServiceTest {

    @Test
    public void shouldMeasure() {
        MetricsService metricsService = new MetricsService();
        TestUpdate update = TestUpdate.ok("name", 1000);
        metricsService.updateMetrics(update);
        Metrics metrics = metricsService.getMetrics();
        System.out.println(metrics.getSuccessCount());
    }

}
