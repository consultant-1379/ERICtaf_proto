package calculator;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ericsson.cifwk.taf.demo.calculator.CalculationResult;
import com.ericsson.cifwk.taf.demo.calculator.ClientRequestData;

@Ignore
public class RestTest {

	static final String ROOT_URL = "http://localhost:8080/calculator/";

	@Test
	public void testAdd() throws Exception {

		ClientRequestData clientRequestData = new ClientRequestData();
		clientRequestData.setVariableOne(1.5);
		clientRequestData.setVariableTwo(2.5);

		ClientRequest request = new ClientRequest(ROOT_URL + "rest/add");

		request.accept(MediaType.APPLICATION_JSON);
		request.body(MediaType.APPLICATION_JSON, clientRequestData);

		ClientResponse<CalculationResult> response = request
				.post(CalculationResult.class);

		CalculationResult entity = response.getEntity();

		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(new BigDecimal("4.0"), entity.getResult());
	}

	@Test
	public void testSubtract() throws Exception {

		ClientRequestData clientRequestData = new ClientRequestData();
		clientRequestData.setVariableOne(5.5);
		clientRequestData.setVariableTwo(2.5);

		ClientRequest request = new ClientRequest(ROOT_URL + "rest/subtract");

		request.accept(MediaType.APPLICATION_JSON);
		request.body(MediaType.APPLICATION_JSON, clientRequestData);

		ClientResponse<CalculationResult> response = request
				.post(CalculationResult.class);

		CalculationResult entity = response.getEntity();

		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(new BigDecimal("3.0"), entity.getResult());
	}

	@Test
	public void testDivide() throws Exception {

		ClientRequestData clientRequestData = new ClientRequestData();
		clientRequestData.setVariableOne(6.0);
		clientRequestData.setVariableTwo(3.0);

		ClientRequest request = new ClientRequest(ROOT_URL + "rest/divide");

		request.accept(MediaType.APPLICATION_JSON);
		request.body(MediaType.APPLICATION_JSON, clientRequestData);

		ClientResponse<CalculationResult> response = request
				.post(CalculationResult.class);

		CalculationResult entity = response.getEntity();

		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(new BigDecimal("2.0"), entity.getResult());
	}

	@Test
	public void testMultiply() throws Exception {

		ClientRequestData clientRequestData = new ClientRequestData();
		clientRequestData.setVariableOne(6.0);
		clientRequestData.setVariableTwo(3.0);

		ClientRequest request = new ClientRequest(ROOT_URL + "rest/multiply");

		request.accept(MediaType.APPLICATION_JSON);
		request.body(MediaType.APPLICATION_JSON, clientRequestData);

		ClientResponse<CalculationResult> response = request
				.post(CalculationResult.class);

		CalculationResult entity = response.getEntity();

		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(new BigDecimal("18.0"), entity.getResult());
	}

}
