package com.ericsson.eiffel.er.rest.resource;

import javax.annotation.ManagedBean;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@ManagedBean
@Path("/query/mongodb_dsl")
public class MongoQueryResource extends RestResource {
	@GET
	@Produces("application/json")
	public String post() {
		return "{\"array\": [],\"int\": 123}";
	}

}
