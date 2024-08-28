package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import org.testng.annotations.Test;

public class FailureTest extends TorTestCaseHelper {

    @Test
    public void shouldFail() {
        fail();
    }

}
