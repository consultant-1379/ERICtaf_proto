package calculator;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ericsson.cifwk.taf.demo.calculator.api.Calculator;

@Ignore
public class EjbTest {

	private static Calculator calculator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		JndiLookup jndiLookup = new JndiLookup();
		calculator = jndiLookup.lookupRemoteStatelessCalculator();
	}

	@Test
	public void addTest() throws Exception {

		BigDecimal resultFromRemoteEjb = calculator.add(1.0, 1.5);

		Assert.assertEquals(new BigDecimal(2.5), resultFromRemoteEjb);

	}

	@Test
	public void subtractTest() throws Exception {

		BigDecimal resultFromRemoteEjb = calculator.subtract(5.5, 2.5);

		Assert.assertEquals(new BigDecimal(3.0), resultFromRemoteEjb);

	}

	@Test
	public void divideTest() throws Exception {

		BigDecimal resultFromRemoteEjb = calculator.divide(6.0, 3.0);

		Assert.assertEquals(new BigDecimal(2.0), resultFromRemoteEjb);

	}

	@Test
	public void multiplyTest() throws Exception {

		BigDecimal resultFromRemoteEjb = calculator.multiply(6.0, 3.0);

		Assert.assertEquals(new BigDecimal(18.0), resultFromRemoteEjb);

	}

}
