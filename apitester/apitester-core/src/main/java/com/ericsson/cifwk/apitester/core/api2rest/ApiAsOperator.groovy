package com.ericsson.cifwk.apitester.core.api2rest

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.util.Map;

import javax.ejb.Remote
import javax.ejb.Stateless
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import com.ericsson.cifwk.taf.apitester.ApiExecutor
import com.ericsson.cifwk.taf.apitester.GroovyParserRemote


/**
 * 
 *	EJB to provide wrapped API execution of class loaded dynamically from a file. Class is processed by CDI as bean and stored on local ClassLoader level.
 *  For detailed usage please refer to TAL Development Guidelines
 */
@Stateless
@Remote([ApiExecutor,GroovyParserRemote])
class ApiAsExecutor implements GroovyParserRemote, ApiExecutor,Serializable {

    GroovyClassLoader gcl = new GroovyClassLoader()

    @Inject
    BeanManager mgr
    private static final long serialVersionUID = -5292763876665709946L
    def apiExecutionWrapper
    Long executionTime

    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.ApiExecutor#instantiate(java.io.File)
    */
    @Override
    public void instantiate(File executorContent) {
        apiExecutionWrapper = null
        apiExecutionWrapper = parseObject(executorContent.readLines().join("\n"))
    }

    
    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.ApiExecutor#executeMethod(java.langString, java.util.Map)
    */
    @Override
    public Object executeMethod(String methodName, Map parameters) {
        def result
        List parametersList = parameters.values().toList()
        Long startTime
        try {
            startTime = System.nanoTime()
            if (! apiExecutionWrapper)
                throw new Exception("API wrapper not initialized. Please make sure proper wrapper file has been created and uploaded.")
            result = apiExecutionWrapper."$methodName"(*parametersList)
            executionTime = System.nanoTime() - startTime
        } catch (Exception e) {
            executionTime = System.nanoTime() - startTime
            result = "$e\n${e.stackTrace.join('\n')}"
        } finally {
            return result
        }
    }
    
    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.GroovyParserRemote#parseClass(java.lang.String)
    */
    @Override
    public Class parseClass(String scriptContent) {
        gcl.parseClass(scriptContent)
    }

    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.GroovyParserRemote#parseObject(java.lang.String)
    */
    @Override
    public Object parseObject(String scriptContent){
        def clazz = parseClass(scriptContent)
        def injectionTarget = mgr.createInjectionTarget(mgr.createAnnotatedType(clazz))
        def ctx = mgr.createCreationalContext(null)
        def instance = injectionTarget.produce(ctx)
        injectionTarget.inject(instance, ctx)
        injectionTarget.postConstruct(instance)
        return instance
    }

    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.GroovyParserRemote#releaseObject(java.lang.Object)
    */
    @Override
    public void releaseObject(Object objectToBeReleased){
        def injectionTarget = mgr.createInjectionTarget(mgr.createAnnotatedType(objectToBeReleased.class))
        injectionTarget.preDestroy(objectToBeReleased)
        injectionTarget.dispose(objectToBeReleased)
        objectToBeReleased = null
    }

    /*
    * (non-Javadoc)
    * @see com.ericsson.cifwk.taf.apitester.ApiExecutor#prepareClasses(java.io.File[])
    */
    @Override
    public void prepareClasses(File... helperClasses) {
        helperClasses.each {
            parseClass(it.readLines().join("\n"))
        }
    }


   

}
