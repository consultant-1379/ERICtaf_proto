package com.ericsson.torsf.caching;


//import javax.annotation.PostConstruct;
//import javax.cache.Cache;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.inject.Inject;
//
//import com.ericsson.oss.itpf.sdk.cache.CacheMode;
//import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy;
//import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
//import com.ericsson.oss.itpf.sdk.cache.modeling.annotation.ModeledNamedCache;
//
//
//@Singleton
//@Startup
//public class SingletonSingleTestBean {
//
//	@Inject
//	@NamedCache("singletonSingleTestConfig")
//	private Cache<String, String> cache;
//
//	@PostConstruct
//	public void init() {
//		System.out.println("------------------------------------I am here "
//				+ cache);
//		System.out.println("tmpDir=" + System.getProperty("java.io.tmpdir"));
//	}
//
//	public String getCacheName() {
//		return "singletonSingleTestConfig";
//	}
//
//	public Object get(String name) {
//		return cache.get(name);
//	}
//
//	public void put(String name, Object value) {
//		cache.put(name, (String) value);
//	}
//
//	public void delete(String name) {
//		cache.remove(name);
//	}

//}
