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
package com.ericsson.torsf.rest.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import com.ericsson.torsf.caching.ReplicatedCacheLRU;


@Path("/cacheRS/ReplicatedCache_LRU")
@ApplicationScoped
public class CacheRestService {
	
    @Inject
    private ReplicatedCacheLRU cache;
    
    @Inject
	Logger logger;
    

    //http://127.0.0.1:8080/apitester-core/rest/cacheRS/ReplicatedCache_LRU/
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    public String init() {
        return "Cache Rest Service for ReplicatedCache_LRU up and running!!!"+cache;
    }

    //http://127.0.0.1:8080/apitester-core/rest/cacheRS/ReplicatedCache_LRU/get
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("get")
    public String get(@QueryParam("propertyName") final String propertyName) {
      return cache.get(propertyName).toString();
    }

  //http://127.0.0.1:8080/apitester-core/rest/cacheRS/ReplicatedCache_LRU/put
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("put")
    public String put(@QueryParam("load") final String noCells) {
    	cache.load(noCells);
    	return String.format("Put %s cells into the cache",noCells);
    }

  //http://127.0.0.1:8080/apitester-core/rest/cacheRS/ReplicatedCache_LRU/getCacheName
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getCacheName")
    public String getCacheName() {
        return cache.name();
    }
    
  //http://127.0.0.1:8080/apitester-core/rest/cacheRS/ReplicatedCache_LRU/get
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getPCICellSize")
    public String getPCICellSize(@QueryParam("cells") final String cells,@QueryParam("cycles") final String cycles) {
      return cache.getPCICellSize(cells, cycles);
    }

}
