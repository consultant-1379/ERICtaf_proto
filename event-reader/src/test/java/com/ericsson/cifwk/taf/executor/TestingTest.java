package com.ericsson.cifwk.taf.executor;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.executor.annotations.TcId;
public class TestingTest {

	@Test
	@TcId(id="ID",title="Title") 
	public void passTest(){
		assert true;
	}
	
	@Test() 
	public void failTest(){
		assert false;
	}
	
	@Test(dependsOnMethods={"failTest"})
	public void skippedTest(){
		assert true;
	}
	private static int cnt = 0;
	
	@Test(invocationCount =2, successPercentage = 50)
	public void passConditionally(){
		cnt++;
		assert cnt > 1;
	}
	
}
