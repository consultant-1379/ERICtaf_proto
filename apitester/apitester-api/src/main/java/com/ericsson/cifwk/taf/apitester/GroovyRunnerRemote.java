package com.ericsson.cifwk.taf.apitester;

import javax.ejb.Remote;

/**
 * Interface for executing Groovy script inside EJB application. Intention is to provide GroovyScriptEngine mechanism allowing to run not compiled Groovy script inside AS 
 * 
 */
@Remote
public interface GroovyRunnerRemote {

    /**
     * Method to run the script using Groovy Script Engine
     * 
     * @param script
     * @return
     * @throws Exception
     */
    Object runScript(String script) throws Exception;

}
