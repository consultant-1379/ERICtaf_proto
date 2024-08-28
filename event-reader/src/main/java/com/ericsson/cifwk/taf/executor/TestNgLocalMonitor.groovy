package com.ericsson.cifwk.taf.executor

import org.testng.IExecutionListener;

/**
 * 
 * Class starting and stopping SimpleMonitor as part of execution 
 */
class TestNgLocalMonitor implements IExecutionListener {

	SimpleMonitor mon = new SimpleMonitor()
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onExecutionStart() {
		mon.start();
		
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onExecutionFinish() {
		mon.close();
	}

}
