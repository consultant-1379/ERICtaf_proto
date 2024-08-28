package com.ericsson.cifwk.taf.api.prototype;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.api.prototype.getters.GreeterGetter;
import com.ericsson.cifwk.taf.api.prototype.operators.GreeterOperator;
import com.ericsson.cifwk.taf.api.prototype.operators.api.ApiGreeterOperatorTest;

public class GreeterTest extends Arquillian {
	GreeterOperator op2 = new GreeterOperator();
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeter.class, PhraseBuilder.class, GreeterTest.class, GreeterOperator.class, ApiGreeterOperatorTest.class, GreeterGetter.class)
		        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		    System.out.println(jar.toString(true));
		    return jar;
	}
	
	@Test(dataProvider="names")
	public void should_create_greeting(String name) {
		Assert.assertEquals(op2.expectedGreeting(name), op2.greeting(name));
	}

	@DataProvider(name="names")
	public static Object[][] names(){
		Object[][] result = new Object[2][1];
		result[0][0] = "Earthling";
		result[1][0] = "Arqullian";
		return result;
	}


}