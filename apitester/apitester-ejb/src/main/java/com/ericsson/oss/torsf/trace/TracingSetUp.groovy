/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.torsf.trace;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.ericsson.oss.itpf.sdk.el.ELResolver;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Startup
public class TracingSetUp implements Serializable {

    private static final long serialVersionUID = -5031234546517629642L;

    @Inject
    ELResolver resolver;

    /**
     * Expression must be in  the format ${configured.aParameter}
     * The 'aParameter' doesn't have to exist.
     * @param expression
     * @return
     */
    @PostConstruct
    public void Alive(){
        println "TracingSetUp, EJB is Alive"
    }

    public String doNothing(String param, int noOfCalls, String tracingLevel){
        println "NOTHING $param"
        return "NOTHING: $param"
    }

    public String resolveExpression(String expression, String load){
        int noOfCalls = Integer.parseInt(load)
        for( i in 0..noOfCalls) {
            final Object value = resolver.resolveExpression(expression);
            println "Resolved Value is: ${value}, iteration: $i"
        }
		return "resolvedExpression!!"
    }
}