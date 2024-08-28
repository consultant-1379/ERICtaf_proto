package com.ericsson.torsf.caching

class ConcurrentRun {

	def userTransaction
	int vusers


	Map<String,String> results = [:]
	public ConcurrentRun(def operator, int vusers,String key, String value){

		userTransaction = { name ->
			results[name] = operator.runSteps(key,value)
		}
	}

	public String execute(){
		List vusersList = []
		vusers.times { index->
			vusersList << Thread.start("vuser $index") { userTransaction("vuser: $index") }
		}

		while (vusersList.find {it.isAlive()}){
			Thread.sleep(20)
		}
		return results.collect { "$it.key: $it.value " }.join("\n")
	}
}
