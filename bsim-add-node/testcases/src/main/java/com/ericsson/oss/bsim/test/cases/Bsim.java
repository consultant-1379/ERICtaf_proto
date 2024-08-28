package com.ericsson.oss.bsim.test.cases;

import java.io.IOException;

import junit.framework.Assert;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.oss.bsim.operators.BsimOperator;
import com.ericsson.oss.bsim.test.data.BsimTestData;

public class Bsim extends TorTestCaseHelper {

    @BeforeSuite
    public static void prepareTheRun() throws ContainerNotReadyException, IOException {
        BsimOperator.prepareCex();
    }

    @Test(dataProvider = "newNodes", dataProviderClass = BsimTestData.class)
    public void addNode(String nodeName, String btsFqn, boolean expectedResult) {
        BsimOperator bsim = new BsimOperator();
        Assert.assertEquals(expectedResult, bsim.addNode(nodeName, btsFqn));
    }
}
