package com.ericsson.cifwk.taf.apitester;

import java.io.File;
import java.util.Map;

/**
 * Interface for executing generic API calls inside AS 
 * Intention is to provide implementation allowing to instantiate file as a CDI-processed bean inside AS and execute methods by the name tracking execution time 
 *
 */
public interface ApiExecutor {

    /**
     * Instatiation pre-step to prepare classes that can be used during main wrapper creation
     * @param helperClasses
     */
    void prepareClasses(File... helperClasses);
    
    /**
     * Instatiation of an engine to execute API calls. ExecutorContent file needs to inject or instantiate all the required objects as well as 
     * provide public methods "executeMethod" operations
     * @param executorContent
     */
    void instantiate(File executorContent);

    /**
     * Executes method that was provided in instantiated file. Parameters are always provided as a Map
     * @param methodName
     * @param parameters
     * @return
     */
    Object executeMethod(String methodName, Map parameters);

    /**
     * Get time of last methods execution in nanoseconds
     * @return
     */
    Long getExecutionTime();


}
