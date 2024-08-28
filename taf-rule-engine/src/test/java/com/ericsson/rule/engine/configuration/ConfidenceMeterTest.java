package com.ericsson.rule.engine.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ConfidenceMeterTest {

    private ConfidenceMeter meter;

    @Before
    public void setUp() throws Exception {
        meter = new ConfidenceMeter();
    }

    @Test
    public void shouldBeOk() throws Exception {
        meter.setUserStoryStatus("1", true);
        meter.setTestCaseStatus("1", "test1", true);
        assertEquals(100, meter.currentValue());
    }

    @Test
    public void shouldBeZero() throws Exception {
        meter.setUserStoryStatus("1", true);
        assertEquals(0, meter.currentValue());
    }

    @Test
    public void shouldBeHalf() throws Exception {
        meter.setUserStoryStatus("1", false);
        meter.setTestCaseStatus("1", "test1", true);
        assertEquals(100, meter.currentValue());
    }

    @Test
    public void shouldStoryBeUnfinished() throws Exception {
        meter.setUserStoryStatus("1", true);
        meter.setTestCaseStatus("1", "test1", false);
        assertEquals(0, meter.currentValue());
    }

    @Test
    public void shouldSupportMoreStories() throws Exception {
        meter.setUserStoryStatus("1", true);
        meter.setUserStoryStatus("2", true);
        meter.setTestCaseStatus("1", "test1", true);
        meter.setTestCaseStatus("1", "test2", false);
        assertEquals(25, meter.currentValue());
    }

    @Test
    public void shouldWorkInCombination() throws Exception {
        meter.setUserStoryStatus("1", true);
        meter.setUserStoryStatus("2", false);
        meter.setTestCaseStatus("1", "test1", true);
        meter.setTestCaseStatus("2", "test2", false);
        assertEquals(33, meter.currentValue());
    }

    @Test
    public void shouldWorkInCombination2() throws Exception {
        meter.setUserStoryStatus("1", false);
        meter.setUserStoryStatus("2", true);
        meter.setTestCaseStatus("1", "test1", true);
        meter.setTestCaseStatus("2", "test2", false);
        assertEquals(66, meter.currentValue());
    }

    @Test
    public void shouldSupportMoreTests() throws Exception {
        meter.setUserStoryStatus("1", true);
        meter.setTestCaseStatus("1", "test1", true);
        meter.setTestCaseStatus("1", "test2", false);
        assertEquals(50, meter.currentValue());
    }

}
