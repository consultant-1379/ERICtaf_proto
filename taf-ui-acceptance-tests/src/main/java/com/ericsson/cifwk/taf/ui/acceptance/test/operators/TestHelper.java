package com.ericsson.cifwk.taf.ui.acceptance.test.operators;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestHelper {
	public static int getJettyPort() {
		InputStream propFileStream = TestHelper.class.getClassLoader().getResourceAsStream("ui_tests.properties");
		Properties props = new Properties();
		try {
			props.load(propFileStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return Integer.parseInt(props.getProperty("jetty.port"));
	}
}
