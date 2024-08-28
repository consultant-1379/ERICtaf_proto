package com.ericsson.cifwk.taf.testng;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class VusersTest {

	private static final Logger log = Logger.getLogger(VusersTest.class);

	@BeforeSuite
	public static void bs() {
		log.info("BS called from " + Thread.currentThread().getName());
	}

	@BeforeClass
	public void bc() {
		log.info("BC calledfrom " + Thread.currentThread().getName());
	}

	@BeforeMethod
	public void bm() {
		log.info("BM called from " + Thread.currentThread().getName());
	}

	@Test
	public void t1() {
		log.info("T1 called from " + Thread.currentThread().getName());
	}

	@Test
	public void t2() {
		log.info("T2 called from " + Thread.currentThread().getName());
	}

	@AfterMethod
	public void am() {
		log.info("AM called from " + Thread.currentThread().getName());
	}

	@AfterClass
	public void ac() {
		log.info("AC called from " + Thread.currentThread().getName());
	}

	@AfterSuite
	public static void as() {
		log.info("AS called from " + Thread.currentThread().getName());
	}
}
