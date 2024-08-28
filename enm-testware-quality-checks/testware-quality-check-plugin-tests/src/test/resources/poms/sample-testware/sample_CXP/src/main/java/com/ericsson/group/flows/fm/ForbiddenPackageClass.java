package com.ericsson.group.flows.fm;

import com.ericsson.cifwk.taf.annotations.TestId;
import org.testng.annotations.Test;

public class ForbiddenPackageClass {

    @TestId(id = "TEST_CASE1")
    @Test
    public void happyPath() {
        System.out.println("Hello world");
    }
}
