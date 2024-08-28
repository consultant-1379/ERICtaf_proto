package com.sandwich;

import com.ericsson.cifwk.taf.spi.TafPlugin;
import com.ericsson.cifwk.taf.testng.CompositeTestNGListener;

public class TestNGKoansTafPlugin implements TafPlugin{

    @Override
    public void init() {
        CompositeTestNGListener.addListener(new TestNGMethodListener(), 54);
    }

    @Override
    public void shutdown() {

    }
}
