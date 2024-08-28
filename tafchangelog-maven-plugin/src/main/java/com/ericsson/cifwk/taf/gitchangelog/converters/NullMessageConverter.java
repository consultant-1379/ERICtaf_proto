package com.ericsson.cifwk.taf.gitchangelog.converters;

import com.ericsson.cifwk.taf.gitchangelog.converters.MessageConverter;

public class NullMessageConverter implements MessageConverter {
	@Override
	public String formatMessage(String original) {
		return original;
	}
}
