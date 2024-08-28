/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.torsf.rest.trace;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

import com.ericsson.oss.torsf.trace.TracingSetUp;

@Path("/trace")
@ApplicationScoped

public class TraceRestService {
		
	    @Inject
	    private TracingSetUp trace;	    

	  //http://127.0.0.1:8080/apitester-core/rest/trace/
	    @GET
	    @Path("/")
	    public String init() {
	        return "Trace Rest Service up and running!!!"+trace;
	    }
	    
	    @GET
	    @Path("/resolveExpression")
	    public String resolveExp(@QueryParam("expression") final String expression, @QueryParam("load") final String load) {
	    	return trace.resolveExpression(expression, load);
	    }
}
