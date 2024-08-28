package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import org.testng.annotations.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CrashTest extends TorTestCaseHelper {

    @Test
    public void shouldCrashJvm() throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        ((Unsafe) theUnsafe.get(null)).getByte(0);
    }

}
