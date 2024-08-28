package com.ericsson.duraci.test.config;

import com.ericsson.duraci.logging.JavaLoggerEiffelLog;

public class SilentEiffelLogger extends JavaLoggerEiffelLog {

	public SilentEiffelLogger(Class<?> cls) {
		super(cls);
	}

	@Override
	public void debug(String msg) {
	}

	@Override
	public void info(String msg) {
	}

	@Override
	public void error(String msg) {
	}
}
