package com.ericsson.duraci.eiffelmessage.messages.events;

import com.ericsson.duraci.configuration.EiffelConfiguration;
import com.ericsson.duraci.datawrappers.Arm;
import com.ericsson.duraci.datawrappers.MessageBus;
import com.ericsson.duraci.eiffelmessage.sending.MessageSender;
import com.ericsson.duraci.eiffelmessage.sending.exceptions.EiffelMessageSenderException;

public class Sender {
    public static final String EXCHANGE = "eiffel.poc.testing";
    public static final String DOMAIN = "diagnostic";
    private MessageSender sender;
    private final MessageBus mb;

    private MessageSender getSender() {
        if (sender == null) {
            final EiffelConfiguration cfg = new EiffelConfiguration() {

                public MessageBus getMessageBus() {
                    return mb;
                }

                public String getDomainId() {
                    return DOMAIN;
                }

                public Arm getArm() {
                    return null;
                }
            };
            sender = new MessageSender.Factory(cfg).create();
        }
        return sender;
    }

    /**
     * Sender constructor requires Message Bus host in AMQP format
     * 
     * @param mbHost
     */
    public Sender(final String mbHost) {
        mb = new MessageBus(mbHost, EXCHANGE);
    }

    public void send(EiffelMonitoringDataEvent event) {
        try {
            getSender().send(event);
        } catch (EiffelMessageSenderException e) {
        }
    }

    public void dispose() {
        if (sender != null)
            sender.dispose();
    }
}
