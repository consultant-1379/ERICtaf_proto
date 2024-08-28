package calculator;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ericsson.cifwk.taf.demo.calculator.api.Calculator;
import com.ericsson.cifwk.taf.demo.calculator.ejb.CalculatorImpl;

public class JndiLookup {
	public Calculator lookupRemoteStatelessCalculator()
			throws NamingException {

		final Hashtable jndiProperties = new Hashtable();
		jndiProperties.put(Context.URL_PKG_PREFIXES,
				"org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);

		return (Calculator) context.lookup("ejb:/calculator//"
				+ CalculatorImpl.class.getSimpleName() + "!"
				+ Calculator.class.getName());
	}

}
