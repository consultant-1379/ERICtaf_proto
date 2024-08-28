package com.ericsson.duraci.test.config;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.duraci.configuration.EiffelConfiguration;
import com.ericsson.duraci.datawrappers.Arm;
import com.ericsson.duraci.datawrappers.MessageBus;
import com.ericsson.duraci.datawrappers.MessageSendQueue;
import com.google.common.base.Preconditions;

public class PerformanceTestEiffelConfiguration implements EiffelConfiguration {
    
    public Arm getArm() {
        return null;
    }

    public String getDomainId() {
    	String domainId = (String) DataHandler.getAttribute("er.mb.domainId");
    	Preconditions.checkNotNull(domainId, "Cannot find Property: er.mb.domainId");
    	return domainId;
    }

    public MessageBus getMessageBus() {
    	String exchangeName = (String) DataHandler.getAttribute("er.mb.exchangeName");
    	Preconditions.checkNotNull(exchangeName, "Cannot find Property: er.mb.exchangeName");
    	String hostName = (String) DataHandler.getAttribute("er.mb.hostName");
    	Preconditions.checkNotNull(exchangeName, "Cannot find Property: er.mb.hostName");
    	
        return new MessageBus(hostName, exchangeName);
    }

	public MessageSendQueue getMessageSendQueue() {
		return new MessageSendQueue(MessageSendQueue.QUEUE_LEN_MAX);
	}
}
