package com.ericsson.duraci.test.operators;

import java.util.List;

import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;

public interface EventRepositoryOperator {

	boolean sendMessage(EiffelEvent event);

	List<Boolean> createLmUpFlow();

	List<Boolean> createUserDefinedFlow();

}