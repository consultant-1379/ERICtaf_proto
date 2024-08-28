package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import org.testng.annotations.Test;

public class HangTest extends TorTestCaseHelper {

    @Test
    public void shouldHang() {
        try {
            Thread.sleep(1000 * 60 * 60);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
