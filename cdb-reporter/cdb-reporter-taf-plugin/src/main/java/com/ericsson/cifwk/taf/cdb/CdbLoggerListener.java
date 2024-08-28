package com.ericsson.cifwk.taf.cdb;

import java.io.File;

import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.ericsson.cifwk.taf.mm.TestNgRunTree;

public class CdbLoggerListener implements IExecutionListener, ISuiteListener {

	private TestNgRunTree runTree;

	private String getExecName() {
		return new File("").getAbsoluteFile().getName();
	}

	public void onExecutionStart() {
		runTree = new TestNgRunTree(getExecName());
	}

	public void onExecutionFinish() {
		runTree.close();
	}

	public void onStart(ISuite suite) {

	}

	public void onFinish(ISuite suite) {
		runTree.addSuite(suite);
	}

}
