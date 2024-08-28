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
package com.ericsson.torsf.caching;

import com.ericsson.oss.itpf.sdk.cache.CacheMode;
import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.cache.modeling.annotation.ModeledNamedCache;

@ModeledNamedCache(name = "ReplicatedCacheLRU", evictionStrategy = EvictionStrategy.LRU, maxEntries = 1000000L, transactional = false,
cacheMode = CacheMode.REPLICATED_SYNC)
public class ReplicatedCacheLRUConfiguraiton {

}
