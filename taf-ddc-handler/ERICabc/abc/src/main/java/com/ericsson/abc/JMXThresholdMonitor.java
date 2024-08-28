package com.ericsson.abc;

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;
import java.text.MessageFormat;
import java.util.Random;

public class JMXThresholdMonitor extends StandardMBean implements ThresholdMonitor {

    public static final Logger LOG = Logger.getLogger(JMXThresholdMonitor.class);
    public static final String JMX_BEAN_NAME = "com.ericsson.abc:type=ThresholdMonitor";


    private JMXThresholdMonitor() {
        super(ThresholdMonitor.class, false);
    }

    public static void start() {
        try {
            ThresholdMonitor mbean = new JMXThresholdMonitor();
            MBeanServer jmxServer = ManagementFactory.getPlatformMBeanServer();
            jmxServer.registerMBean(mbean, new ObjectName(JMX_BEAN_NAME));
            LOG.info(MessageFormat.format("ABC:JMX service [{0}] started", JMX_BEAN_NAME));
        } catch (Exception e) {
            LOG.error(MessageFormat.format("ABC:JMX service [{0}] fail:{1}", JMX_BEAN_NAME, e.getMessage()),e);
            Main.stop();
        }
    }


    Random threshold = new Random();

    @Override
    public int getThreshold() {
        int value = Math.abs(threshold.nextInt() % 10);
        LOG.info(MessageFormat.format("ABC:JMX {0} is : {1}", JMX_BEAN_NAME,value));
        return value;
    }

    @Override
    public void stop() {
        LOG.info(MessageFormat.format("ABC:JMX {0} stop", JMX_BEAN_NAME));
        Main.stop();
    }
}
