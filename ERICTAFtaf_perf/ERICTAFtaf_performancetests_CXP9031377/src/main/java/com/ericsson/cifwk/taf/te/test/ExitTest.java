package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import org.testng.annotations.Test;

public class ExitTest extends TorTestCaseHelper {

    @Test
    public void shouldExit() {
        System.exit(100);
    }

}
