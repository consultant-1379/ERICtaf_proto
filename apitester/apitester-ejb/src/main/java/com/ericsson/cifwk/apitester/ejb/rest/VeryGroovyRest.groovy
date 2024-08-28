package com.ericsson.cifwk.apitester.ejb.rest

import javax.cache.Cache;
import javax.ejb.EJB
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.spi.BeanManager
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.*
import javax.management.*
import javax.naming.InitialContext

import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import com.ericsson.torsf.caching.*
import com.ericsson.torsf.caching.data.PCICell;

import org.slf4j.Logger

@ApplicationScoped
@Path("/vgroovy")
public class VeryGroovyRest {

	@Inject
	Logger logger

//	@EJB
//	SingletonSingleTestBean singleTest

//	@EJB
//	Configuration2SingleTest singleTest2

	@Inject
	ConfigurationsDataProvider cdp

	@Inject
	private BeanManager bm

	//http://127.0.0.1:8080/ tor-test/rest/classLevelProfiledBean/
	@GET
	@Path("/")
	public String init() {
		return "Cache Rest Service for ReplicatedCache_LRU up and running!!!"+simple;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayHello(){
		return Response.ok("Hello from Groovy").build()
	}

	private def getOperator(){
		def gcl = new GroovyClassLoader()
		def config = cdp.configs[0]
		println config
		def configClass = gcl.parseClass(cdp.generateConfigClass(config))
		def operator = cdp.generateCacheOperatorClass(configClass.name)
		def operatorClass = gcl.parseClass(operator)
		def operatorInstance = operatorClass.newInstance()
		return operatorInstance
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("cacheCheck")
	public Response check(){
		String chk0 = "$bm"
		String chk1
		String chk2
		try{
			chk0 += " $cdp"
			chk1 = cdp?.nm?.toString()
			chk2 = operator?.bm?.toString()
		} catch (Exception e) {
			// TODO: handle exception
		}
		return Response.ok("$chk0\n $chk1 \n $chk2").build()
	}



	@GET
	@Path("generateCacheMatrix")
	public String generateCacheMatrix(){
		String destDir = "/tmp/caching"
		"""rm -rf $destDir"""
		"""mkdir -p $destDir/META-INF""".execute().consumeProcessOutput()
		new File("$destDir/META-INF/MANIFEST-MF") << """<?xml version="1.0"?>
		<beans xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://jboss.org/schema/cdi/beans_1_0.xsd">
		</beans>
		"""
		cdp.configs.each { config ->
			File configFile = new File("$destDir/${config[0]}.groovy")
			configFile << cdp.generateConfigClass(config)
			def operator = cdp.generateCacheOperatorClass(config[0])
			new File("$destDir/${config[0]}Operator.groovy") << operator
		}
		return destDir
	}


	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("acceptance")
	public Response acceptanceTest(@QueryParam("key") String key,@QueryParam("value") String content,@QueryParam("delete") boolean delete){
		String responseContent =""
		[singleTest, singleTest2].each { testBean ->

			responseContent += "[$testBean.cacheName] Getting before putting $key: ${testBean.get(key)}\n"
			testBean.put(key, content)
			responseContent +=  "[$testBean.cacheName] Getting $key: ${testBean.get(key)}\n"
			if (delete){
				testBean.delete(key)
				responseContent += "[$testBean.cacheName] Getting after deletion $key: ${testBean.get(key)}\n"
			}
		}
		return Response.ok(responseContent).build()
	}

	@GET
	@Path("jmx")
	public String getMBeans(@QueryParam("name")String name){
		MBeanServer server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0)
		return new GroovyMBean(server,new ObjectName(name)).toString()
	}
	@GET
	@Path("test")
	public Response cacheTest(@QueryParam("configNo") int configNo,@QueryParam("vusers") int vusers, @QueryParam("key") String key,@QueryParam("value") String content){
		String responseContent = ""
		String configName = cdp.configs[configNo][0]
		def operator = new InitialContext().lookup("java:global/cache-configs/${configName}Operator")
		responseContent = new ConcurrentRun(operator,vusers,key,content).execute()
		return Response.ok(responseContent).build()
	}

	@GET
	@Path("finishTest")
	public String finishTest(){
		logger.info "Invoking [TAF] () Invoking Test Case finalization: Test Case Finished"
		logger.info "Succesfully [TAF] () Successfully finished invocation: Test Case finalization"
	}
}
