package com.ericsson.cifwk.taf.demo.calculator;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ericsson.cifwk.taf.demo.calculator.api.Calculator;

@Path("/")
public class RestService {

	@EJB
	Calculator remoteCalculator;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAddResult(ClientRequestData clientRequestData) {

		BigDecimal result = remoteCalculator.add(
				clientRequestData.getVariableOne(),
				clientRequestData.getVariableTwo());

		CalculationResult calculationResult = new CalculationResult();
		calculationResult.setResult(result);

		return Response.status(200).entity(calculationResult).build();
	}

	@POST
	@Path("/subtract")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSubtractResult(ClientRequestData clientRequestData) {

		BigDecimal result = remoteCalculator.subtract(
				clientRequestData.getVariableOne(),
				clientRequestData.getVariableTwo());

		CalculationResult calculationResult = new CalculationResult();
		calculationResult.setResult(result);

		return Response.status(200).entity(calculationResult).build();
	}

	@POST
	@Path("/divide")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDivideResult(ClientRequestData clientRequestData) {

		BigDecimal result = remoteCalculator.divide(
				clientRequestData.getVariableOne(),
				clientRequestData.getVariableTwo());
		CalculationResult calculationResult = new CalculationResult();
		calculationResult.setResult(result);

		return Response.status(200).entity(calculationResult).build();
	}

	@POST
	@Path("/multiply")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMultiplyResult(ClientRequestData clientRequestData) {

		BigDecimal result = remoteCalculator.multiply(
				clientRequestData.getVariableOne(),
				clientRequestData.getVariableTwo());
		CalculationResult calculationResult = new CalculationResult();
		calculationResult.setResult(result);

		return Response.status(200).entity(calculationResult).build();
	}

}
