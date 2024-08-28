package com.ericsson.cifwk.taf.cdb;

import com.ericsson.cifwk.taf.spi.TafPlugin;
import com.ericsson.cifwk.taf.testng.CompositeTestNGListener;

public class CdbLoggerTafPlugin implements TafPlugin {

	public void init() {
		CompositeTestNGListener.addListener(new CdbLoggerListener(), 20);
	}

	public void shutdown() {

	}

}
