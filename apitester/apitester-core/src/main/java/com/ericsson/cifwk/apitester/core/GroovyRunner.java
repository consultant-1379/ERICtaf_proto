package com.ericsson.cifwk.apitester.core;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ericsson.cifwk.taf.apitester.GroovyRunnerRemote;

/**
 * EJB to provide simple mechanism for running Groovy scripts inside AS. Allows to control binding passed to the script. 
 */
@Stateless
public class GroovyRunner implements GroovyRunnerRemote {

	@EJB
	BeanReconciler br;

	Binding scriptBinding;

	/**
	 * Default constructor.
	 */
	public GroovyRunner() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ericsson.cifwk.taf.apitester.GroovyRunnerRemote#runScript(java.lang.String)
	 */
	@Override
	public Object runScript(String script) throws Exception {
		return getEngine().run(script, getBinding(script));
	}
	/**
	 * Creates GroovyScriptEngine with default class loader
	 * @return
	 * @throws IOException
	 */
	private GroovyScriptEngine getEngine() throws IOException {
		return new GroovyScriptEngine(getScriptsRoot(), getClassLoader());
	}
	/**
	 * Wrapping class for a script root directory
	 * @return
	 */
	public String getScriptsRoot() {
		return "";
	}
	/**
	 * Binding preparation putting script name into it
	 * @param script
	 * @return
	 */
	private Binding getInitialBinding(String script) {
		Binding initialBinding = new Binding();
		if (script != null) {
			putScriptNameInBinding(initialBinding, script);
		}
		return initialBinding;
	}
	/**
	 * Binding initializer
	 * @return
	 */
	public Binding getBinding() {
		return getBinding(null);
	}
	/**
	 * Binding initialization with a script file inside it
	 * @param script
	 * @return
	 */
	public Binding getBinding(String script) {
		if (scriptBinding == null) {
			scriptBinding = getInitialBinding(script);
		} else {
			if (script != null) {
				putScriptNameInBinding(scriptBinding, script);
			}
		}
		return scriptBinding;
	}
	/**
	 * Prepare binding to put script name and Bean Reconciler instances inside
	 * @param binding
	 * @param script
	 */
	private void putScriptNameInBinding(Binding binding, String script) {
		binding.setProperty("SCRIPT_NAME", script);
		binding.setProperty("BEAN_RECONCILLER", br);
	}
	/**
	 * Get class' ClassLoader 
	 * @return
	 */
	private ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
}
