package com.ericsson.oss.bsim.data;

import java.util.ArrayList;
import java.util.List;

public class BsimDataProvider {

    public String getNewNodeName() {
        return "Node " + System.currentTimeMillis();
    }

    public List<String> getBtsFdns() {
        List<String> allBtses = new ArrayList<String>();
        allBtses.add("ROOT_MO");
        return allBtses;
    }
}
