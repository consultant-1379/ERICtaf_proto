package com.ericsson.abc;

public interface ThresholdMonitor {
    /**
     * @return random Threshold [0-9]
     */

    public int getThreshold();

    /**
     * Stop application
     */
    public void stop();
}
