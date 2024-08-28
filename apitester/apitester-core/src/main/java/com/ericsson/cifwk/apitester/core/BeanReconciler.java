package com.ericsson.cifwk.apitester.core;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Class to provide tools for managing beans inside Application Server.
 */
@Stateless
@LocalBean
public class BeanReconciler {
    @Inject
    BeanManager bm;

    /**
     * Default constructor.
     */
    public BeanReconciler() {
    }
    /**
     * List beans tree
     * @return
     */
    public List<String> getBeans() {
        ArrayList<String> result = new ArrayList();
        Context ctx;
        try {
            ctx = (Context) new InitialContext().lookup("java:global");
            listContext(ctx, "", result);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Recursively exhaust the JNDI tree
     */
    private final void listContext(Context ctx, String indent,
            List<String> result) {
        try {
            NamingEnumeration list = ctx.listBindings("");
            while (list.hasMore()) {
                Binding item = (Binding) list.next();
                String className = item.getClassName();
                String name = item.getName();
                result.add(indent + className + " " + name);
                Object o = item.getObject();
                if (o instanceof javax.naming.Context) {
                    listContext((Context) o, indent + " ", result);
                }
            }
        } catch (NamingException ex) {
            System.err.println("JNDI failure: " + ex);
        }
    }
    /**
     * Get AS BeanManager
     * @return
     */
    public BeanManager getBeanManager() {
        InitialContext initialContext;
        try {
            initialContext = new InitialContext();

            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
