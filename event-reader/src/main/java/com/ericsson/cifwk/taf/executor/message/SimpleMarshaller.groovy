package com.ericsson.cifwk.taf.executor.message

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.lang.reflect.Method

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SimpleMarshaller {

	static Logger logger = LoggerFactory.getLogger(SimpleMarshaller)
	public static String toXml(def object){
		String result = "<${object.class.simpleName}>\n"
		object.getProperties().findAll {! (it.key in ["class","metaClass"])}.findAll { it.value}each {
			result += "<${it.key}>${it.value}</${it.key}>\n"
		}
		result +="</${object.class.simpleName}>"
		return result;
	}

	public static Object toObject(Class clazz, String data){
		logger.debug "Parsing $data"
		def root
		try {
			JsonSlurper slurper = new JsonSlurper()
			root = slurper.parseText(data)
		} catch (Exception notJson){
			XmlSlurper slurper = new XmlSlurper()
			root = slurper.parseText(data)
		}
		Object result = clazz.newInstance()
		result.getProperties().each { key, value ->
			String val = root[key].toString()
			if (val.size() > 0 && val != "null"){
				Method m = clazz.methods.find {it.name.equalsIgnoreCase("set$key")}
				if (m){
					Class toCast = m.getParameterTypes().first()
					def setVal =  val.asType(toCast)
					result.setProperty(key,setVal)
				}
			}
		}
		logger.debug "Returning $result"
		return result
	}

	public static String toJson(def object){
		return JsonOutput.toJson(object)
	}
}
