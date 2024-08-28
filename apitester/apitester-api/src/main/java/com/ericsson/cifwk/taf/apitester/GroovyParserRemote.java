package com.ericsson.cifwk.taf.apitester;

/**
 * Interface for operations on beans created dynamically in Groovy
 * Intention is to provide implementation allowing creation and CDI-processing of classes specified as a String
 *
 */
public interface GroovyParserRemote{

    /**
     * Parse file in a form of String and return a class
     * @param scriptContent
     * @return
     */
    Class parseClass(String scriptContent);

    /**
     * Parse file in a form of String and return Object
     * @param scriptContent
     * @return
     */
    Object parseObject(String scriptContent);

    /**
     * Release object created by GroovyBeanParser
     * @param objectToBeReleased
     */
    void releaseObject(Object objectToBeReleased);
}
