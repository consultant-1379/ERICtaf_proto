package com.ericsson.cifwk.ve.plugins.eiffel;

import com.ericsson.duraci.configuration.EiffelConfiguration;
import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;
import com.ericsson.duraci.eiffelmessage.messages.EiffelMessage;
import com.ericsson.duraci.eiffelmessage.messages.v2.EiffelMessageImpl;
import com.ericsson.duraci.eiffelmessage.sending.MessageSender;
import com.ericsson.duraci.eiffelmessage.sending.exceptions.EiffelMessageSenderException;
import com.google.common.base.Throwables;

/**
 *
 */
public class Sender {

    private final EiffelConfiguration configuration;
    private MessageSender messageSender;

    public Sender(EiffelConfiguration configuration) {
        this.configuration = configuration;
    }

    public void init() {
        messageSender = new MessageSender.Factory(configuration).create();
    }

    public void send(EiffelEvent event) {
        try {
            messageSender.send(createMessage(event));
        } catch (EiffelMessageSenderException e) {
            throw Throwables.propagate(e);
        }
    }

    private EiffelMessage createMessage(EiffelEvent event) {
        String domainId = configuration.getDomainId();
        return new EiffelMessageImpl(domainId, event);
    }

    public void shutdown() {
        if (messageSender != null) {
            messageSender.dispose();
        }
    }

}
