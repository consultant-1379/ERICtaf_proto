package com.ericsson.cifwk.taf.api.prototype.operators.api;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.api.prototype.Greeter;
import com.ericsson.cifwk.taf.api.prototype.GreeterTest;
import com.ericsson.cifwk.taf.api.prototype.PhraseBuilder;
import com.ericsson.cifwk.taf.api.prototype.getters.GreeterGetter;
import com.ericsson.cifwk.taf.api.prototype.operators.GreeterOperator;

public class ApiGreeterOperatorTest  extends Arquillian{
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeter.class, PhraseBuilder.class, GreeterTest.class, GreeterOperator.class, ApiGreeterOperatorTest.class, GreeterGetter.class)
		        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		    System.out.println(jar.toString(true));
		    return jar;
	}
	
	
	@Inject
	Greeter greeter;
	
	PrintStream testOutput;
	private PrintStream getTestOutput(){
		if (testOutput == null){
				OutputStream out = new ByteArrayOutputStream();		
				testOutput = new PrintStream(out);
		}
		return testOutput;
	}

	@Test
	public void doNothing(){
		greeter.greet(System.out,"local");
	}
	
	public String greeting(String name) {
		greeter.createGreeting(name);
		greeter.greet(getTestOutput(),name);
		return testOutput.toString();
	}

}
