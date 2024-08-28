package com.ericsson.torsf.caching;


import javax.cache.Cache;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.cache.CacheMode;
import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import com.ericsson.oss.itpf.sdk.cache.modeling.annotation.ModeledNamedCache;

@ModeledNamedCache(name = "singleTestConfig2", evictionStrategy = EvictionStrategy.LIRS, maxEntries = 5L, transactional = true, cacheMode = CacheMode.REPLICATED_ASYNC, numberOfDistributedOwners = 3, timeToLive = 10000L)
@Stateless
public class Configuration2SingleTest {

	@Inject
	@NamedCache("singleTestConfig2")
	private Cache<String, String> cache;

	public String getCacheName() {
		return "singleTestConfig2";
	}

	public Object get(String name) {
		return cache.get(name);
	}

	public void put(String name, Object value) {
		cache.put(name, (String) value);
	}

	public void delete(String name) {
		cache.remove(name);
	}
}
