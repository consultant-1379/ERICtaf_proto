package com.ericsson.cifwk.apitester.core.rest

import javax.ejb.EJB
import javax.enterprise.context.ApplicationScoped

import javax.ws.rs.*
import javax.ws.rs.core.*

import com.ericsson.cifwk.taf.apitester.ApiExecutor

/**
 * REST application interface integrating {@link ApiExecutor} for execution part with REST interface.
 * Intention is to use this  together with TAL's API implementation for executing API tests inside AS.
 * For details please refer to TAL Development Guidelines
 *
 */
@ApplicationScoped
@Path("/api")
class MetaGroovyRest {

	@EJB
	ApiExecutor apiExecutor

	/**
	 * Preparation method for API execution. Consumes a file containing API-wrapper that will be used for invocation of methods 
	 * @param uploadData
	 * @return
	 */
	@POST
	@Path("file")
	@Consumes( "multipart/form-data" )
	public String file(Map<String,File> uploadData){
		String result = ""
		File file = uploadData.'file'
		String fileContent = file.readLines().join("\n")
		try {
			apiExecutor.instantiate(file)		
		} catch (Exception e) {
			result = "$e\n${e.stackTrace.join('n')}"
			e.printStackTrace() 
		}
		return result
	}
	
	/**
	 * Execute method on object created in "file" step. Expects method name and parameters specified as map of format key1:value1,key2:value2. Currently only values are used in the invoked method. Returns a String containing XML of form <response><result/><time/></response>
	 * @param method
	 * @param params
	 * @return
	 */
	@GET
	@Path("execute")
	public String execute(@QueryParam("method") String method,@QueryParam("params")String params){
		
		def paramsMap = params.split(',').inject([:]) { map, token ->
			token.split(':').with { map[it[0]] = it[1] }
			map
		}

		def result = apiExecutor.executeMethod(method, paramsMap)

		return """<response>
<result>$result</result>
<time>${apiExecutor.executionTime}</time>
</response>
"""
	}
}
