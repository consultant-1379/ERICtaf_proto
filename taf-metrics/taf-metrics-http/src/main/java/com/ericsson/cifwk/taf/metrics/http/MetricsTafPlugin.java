package com.ericsson.cifwk.taf.metrics.http;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.spi.TafPlugin;
import com.ericsson.cifwk.taf.testng.CompositeTestNGListener;
import com.ericsson.cifwk.taf.tools.http.impl.HttpToolListeners;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.util.UUID;

public class MetricsTafPlugin implements TafPlugin {

    private MetricsHttpToolListener httpToolListener;

    @Override
    public void init() {
        String executionId = getStringAttr("ER_REPORTING_PARENT_EXECUTION_ID", UUID.randomUUID().toString());
        String amqpUrl = getStringAttr("taf.performance.metrics.amqp.url", null);
        String amqpExchange = getStringAttr("taf.performance.metrics.amqp.exchange", null);

        CompositeTestNGListener.addListener(new MetricsTestListener(), 50);
        httpToolListener = new MetricsHttpToolListener(executionId, amqpUrl, amqpExchange);
        HttpToolListeners.addListener(httpToolListener);
        try {
            httpToolListener.initialize();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static String getStringAttr(String key, String defaultValue) {
        Object value = DataHandler.getAttribute(key);
        if (value == null) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new NullPointerException("Required data handler attribute " + key + " not provided");
        }
        return value.toString();
    }

    @Override
    public void shutdown() {
        if (httpToolListener != null) {
            try {
                httpToolListener.shutdown();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    }

}
