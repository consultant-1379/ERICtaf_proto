package com.ericsson.cifwk.apitester.ejb.rest

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.*

import com.ericsson.torsf.caching.ConfigurationsDataProvider

@ApplicationScoped
@Path("/cache")
public class CacheRest {

	@Inject
	ConfigurationsDataProvider cdp

	private String runTest(def operator,String key,String value, String vusers){
		List<String> messages
		def singleRun = {
			try {
				messages << operator.newInstance().runSteps(key,value)
			} catch (Exception e) {
				messages << e.message
			}
		}
		List vusersRunning = []
		vusers.toInteger().times {
			vusersRunning << Thread.start {singleRun()}
		}

		while (vusersRunning.find(it.state() != Thread.State.TERMINATED)){
			Thread.sleep(20)
		}
		String result = ""
		messages.eachWithIndex { idx, msg ->
			result +="""

			vuser no: $idx, message: $msg

"""
		}
		return result
	}

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response testCache(String configNo, String vusers, String key, String data) {

		return Response.ok("").build()
	}

	@GET
	@Path("configs")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getConfigs() {
		return Response.ok(cdp.getConfigs().collect { it.join()}.join("\n")).build()
	}
}
