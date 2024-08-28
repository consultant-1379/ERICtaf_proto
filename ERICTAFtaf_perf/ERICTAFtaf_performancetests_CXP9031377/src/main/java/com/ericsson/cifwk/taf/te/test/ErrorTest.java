package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import org.testng.annotations.Test;

public class ErrorTest extends TorTestCaseHelper {

    @Test
    public void shouldThrowException() {
        throw new RuntimeException("exception");
    }

}
