package com.ericsson.abc.test.cases;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.performance.threshold.DataWatcher;
import com.ericsson.cifwk.taf.performance.threshold.ThresholdHandler;
import com.ericsson.cifwk.taf.performance.threshold.ThresholdRule;
import com.ericsson.cifwk.taf.performance.threshold.ViolationListener;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.text.MessageFormat;

public class ABCThresholdMonitoring extends TorTestCaseHelper implements TestCase {

    public static final int MAX_VIOLATIONS = 5;
    public static final String DDC_METRIC = "tor.{0}.JMX-abc.Threshold";
    public static final int MIN_VALID_THRESHOLD = 1;
    public static final int MAX_VALID_THRESHOLD = 8;
    public static final int DDC_CHECK_INTERVAL_IN_SECONDS = 10;
    public static final int WAIT_TIME_IN_SECONDS = 60;
    boolean isViolate = false;

    @Test
    public void thresholdMonitoring() throws Exception {
        Host abcHost = DataHandler.getHostByName("abc");
        assertNotNull(abcHost);
        String name = InetAddress.getByName(abcHost.getIp()).getHostName();
        name = name.split("\\.")[0];
        String metric = MessageFormat.format(DDC_METRIC, name);
        Host graphite = DataHandler.getHostByName("graphite");
        assertNotNull(graphite);
        ViolationListener violationListener = new ViolationListener() {
            int violationCount = 0;

            @Override
            public void onViolate(ThresholdRule threshold, long timestamp, double update) {
                System.out.println(MessageFormat.format("For {0} violated:{1}", DDC_METRIC, threshold));
                if (++violationCount > MAX_VIOLATIONS) isViolate = true;
            }
        };
        DataWatcher watcher = DataWatcher.builder()
                .listener(violationListener)
                .min(MIN_VALID_THRESHOLD).max(MAX_VALID_THRESHOLD)
                .build();
        ThresholdHandler service = new ThresholdHandler();
        service.monitor(metric, watcher);
        service.start(graphite, DDC_CHECK_INTERVAL_IN_SECONDS);
        while (!isViolate) {
            service.listen(WAIT_TIME_IN_SECONDS);
        }
        service.stop();
        assertFalse(isViolate);
    }
}
