package com.ericsson.cifwk.taf.grid.shared;


import java.io.Serializable;

/**
 *
 */
public final class TestSchedule implements Serializable {

    private int repeatCount;
    private long from;
    private long until;
    private int vusers;
    private long thinkTime;

    public TestSchedule() {
        this(1, Long.MIN_VALUE, Long.MAX_VALUE, 1);
    }

    public TestSchedule(int repeatCount, int vusers) {
        this(repeatCount, Long.MIN_VALUE, Long.MAX_VALUE, vusers);
    }

    public TestSchedule(int repeatCount, long runFrom, long runUntil, int vusers) {
        this.repeatCount = repeatCount;
        this.from = runFrom;
        this.until = runUntil;
        this.vusers = vusers;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public long getFrom() {
        return from;
    }

    public long getUntil() {
        return until;
    }

    public int getVusers() {
        return vusers;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public void setVusers(int vusers) {
        this.vusers = vusers;
    }

    public long getThinkTime() {
        return thinkTime;
    }

    public void setThinkTime(long thinkTime) {
        this.thinkTime = thinkTime;
    }

    public TestSchedule copy(int vusers) {
        TestSchedule result = new TestSchedule();
        result.repeatCount = this.repeatCount;
        result.from = this.from;
        result.until = this.until;
        result.vusers = vusers;
        result.thinkTime = this.thinkTime;
        return result;
    }

}
