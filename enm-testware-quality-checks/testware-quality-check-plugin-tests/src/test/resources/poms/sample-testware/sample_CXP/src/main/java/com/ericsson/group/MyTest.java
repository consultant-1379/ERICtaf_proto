package com.ericsson.group;

import com.ericsson.cifwk.taf.annotations.TestId;
import org.testng.annotations.Test;

public class MyTest {

    @TestId(id = "TEST_CASE1")
    @Test
    public void happyPath() {
        System.out.println("Hello world");
    }
}
