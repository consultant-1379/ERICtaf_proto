package com.ericsson.torsf.caching

import javax.enterprise.inject.spi.BeanManager
import javax.inject.Inject

import com.ericsson.oss.itpf.sdk.cache.CacheMode
import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy

class ConfigurationsDataProvider {
	private List toCombine = []

	String generateConfigClass(List<String> arguments){
		assert arguments.size()==7 : "Wrong list of arguments"
		generateConfigClass(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5], arguments[6])
	}
	String generateConfigClass(String configName,String evictionStrategy, String maxEntries, String transactional, String cacheMode, String numberOfOwners, String timeToLive){
		String classTemplate = """
import com.ericsson.oss.itpf.sdk.cache.CacheMode;
import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.cache.modeling.annotation.ModeledNamedCache;

@ModeledNamedCache(name = "$configName", evictionStrategy=$evictionStrategy, maxEntries=$maxEntries, transactional=$transactional, cacheMode=$cacheMode ,numberOfDistributedOwners=$numberOfOwners,timeToLive=$timeToLive)
public class $configName {}
"""
	}

	String generateCacheOperatorClass(String configName){
		EvictionStrategy
		return """
import javax.cache.Cache;
import javax.inject.Inject;
import javax.enterprise.inject.spi.BeanManager
import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import javax.ejb.Stateless;

@Stateless
class ${configName}Operator {

	@Inject
	@NamedCache("$configName")
	private Cache<String, Object> cache;

	@Inject
	BeanManager bm;

		public  ${configName}Operator(){
			println bm
		}

		def get(String name){
			return cache.get(name)
		}

		void put(String name,def value){
			cache.put(name,value)
		}

		void delete(String name){
			cache.delete(name)
		}

		String runSteps(String key, def value){
			int initialSize = cache.size()
			int startTime = System.nanoTime()
			put(key,value)
			String putTime = "\${System.nanoTime() - \$startTime}"
			int sizeAfterAdding = cache.size()
			assert  sizeAfterAdding == initialSize +1
			startTime = System.nanoTime()
			assert get(key) == value
			String getTime = "\${System.nanoTime() - \$startTime}"
			startTime = System.nanoTime()
			delete(key)
			String deleteTime = "\${System.nanoTime() - \$startTime}"
			assert cache.size() == sizeAfterAdding -1

			return "PUT:\$putTime GET:\$getTime DELETE:\$deleteTime"
		}

}
"""
	}
	//@ModeledNamedCache(name = "CONFIG_A", evictionStrategy=EvictionStrategy.LRU, maxEntries=100, transactional=true, cacheMode=CacheMode.DISTRIBUTED_SYNC,numberOfDistributedOwners=2,timeToLive=2)

	ConfigurationsDataProvider(){
		Map allCombinations = [:]

		List configurationAnnotationParameters = [
			"evictionStrategy",
			"maxEntries",
			"transactional",
			"cacheMode",
			"numberOfDistributedOwners",
			"timeToLive"
		]

		configurationAnnotationParameters.each { name ->
			List combinationSet = []
			if (name=="evictionStrategy"){
				EvictionStrategy.each { combinationSet << "EvictionStrategy.$it" }

			} else if (name=="maxEntries") {
				combinationSet = [
					"1L",
					"100L",
					"1000L",
					"10000L",
					"100000L",
					"1000000L",
					"10000000L",
					"100000000L"
				]
			} else if (name=="transactional"){
				combinationSet = ["true", "false"]
			} else if (name=="cacheMode"){
				CacheMode.each { combinationSet << "CacheMode.$it" }
			} else if (name=="numberOfDistributedOwners"){
				combinationSet = ["1", "2", "3"]
			} else if (name=="timeToLive"){
				combinationSet = [
					"1L",
					"10L",
					"100L",
					"1000L",
					"10000L"
				]
			}

			allCombinations.put(name,combinationSet)
		}

		allCombinations.each { key, val ->
			toCombine << val
		}



	}
	List getConfigCombinations(){
		return toCombine.combinations()	}

	List<List<String>> getConfigs(){
		List<String> result = []
		getConfigCombinations().each {
			List callArgs = [
				it.toString().replace(", ", "_").replace(".","_").replace("[", "").replace("]","")
			]
			callArgs.addAll(it)
			result << callArgs
		}
		return result
	}
}
