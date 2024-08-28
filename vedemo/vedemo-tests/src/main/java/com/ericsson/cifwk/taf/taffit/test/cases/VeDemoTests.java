package com.ericsson.cifwk.taf.taffit.test.cases;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;

public class VeDemoTests extends TorTestCaseHelper implements TestCase {

	@TestId(id = "CIP-3732", title = "VE demo pass")
	@DataDriven(name = "calculator")
	@Test
	public void TestToPass(@Input("x") int someValue) {
		assertTrue(someValue > 0);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@TestId(id = "CIP-3730", title = "VE demo fail")
	@DataDriven(name = "calculator")
	@Test
	public void TestToFail(@Input("x") int someValue) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(someValue < 0);
	}

	@TestId(id = "CIP-3731", title = "VE demo skip")
	@Test(dependsOnMethods = { "TestToFail" })
	public void TestToBeSkipped() {
		assertTrue(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@TestId(id = "CIP-3788", title = "VE demo pass for other Story")
	@Test
	public void AnotherPassingTest() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@TestId(id = "CIP-3788", title = "VE demo pass for other with different Id Story")
	@Test
	public void YetAnotherPassingTest() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@TestId(id = "CIP-3788", title = "VE demo pass for other with different Id Story")
	@Test
	public void AnotherFailingTest() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(false);
	}
}
