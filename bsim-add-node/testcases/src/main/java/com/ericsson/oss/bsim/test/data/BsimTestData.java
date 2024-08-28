package com.ericsson.oss.bsim.test.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.oss.bsim.data.BsimDataProvider;

public class BsimTestData implements TestData {

    @DataProvider(name = "newNodes")
    public static Iterator<Object[]> addNodeData() {
        BsimDataProvider data = new BsimDataProvider();
        List<Object[]> allInocations = new ArrayList<Object[]>();
        for (String bts : data.getBtsFdns()) {
            Object[] oneTestInvocation = new Object[3];
            oneTestInvocation[0] = data.getNewNodeName();
            oneTestInvocation[1] = bts;
            oneTestInvocation[2] = true;
            allInocations.add(oneTestInvocation);
        }
        return allInocations.iterator();
    }
}
