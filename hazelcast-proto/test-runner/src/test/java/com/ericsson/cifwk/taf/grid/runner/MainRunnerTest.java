package com.ericsson.cifwk.taf.grid.runner;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class MainRunnerTest {

    private MainRunner runner;

    @Before
    public void setUp() {
        runner = new MainRunner();
    }

    @Test
    public void testRunTest() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("className", "com.ericsson.cifwk.taf.grid.runner.MainRunnerTest$TestClass");
        map.put("args", "x;y;z");
        runner.setUp(new IsolatedContainer(), "", map);
        runner.runTest();
        runner.tearDown();
    }

    public static class TestClass {
        public static void main(String[] args) {
            assertThat(args[0], equalTo("x"));
            assertThat(args[1], equalTo("y"));
            assertThat(args[2], equalTo("z"));
            System.out.println(Arrays.toString(args));
        }
    }

}
