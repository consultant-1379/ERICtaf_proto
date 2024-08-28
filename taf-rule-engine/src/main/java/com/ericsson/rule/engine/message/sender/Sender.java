package com.ericsson.rule.engine.message.sender;


import com.ericsson.duraci.configuration.EiffelConfiguration;
import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelConfidenceLevelModifiedEvent;
import com.ericsson.duraci.eiffelmessage.sending.MessageSender;
import com.ericsson.duraci.eiffelmessage.sending.exceptions.EiffelMessageSenderException;

public class Sender {
    private MessageSender sender;
    private EiffelConfiguration conf;

    private MessageSender getSender() {
        if (sender == null) {
            
            sender = new MessageSender.Factory(conf).create();
        }
        return sender;
    }

    /**
     * Sender constructor requires EiffelConfiguration
     * 
     * @param mbHost
     */
    public Sender(final EiffelConfiguration conf) {
        this.conf = conf;
    }

    public void send(EiffelEvent event) {
        try {
            getSender().send(event);
        } catch (EiffelMessageSenderException e) {
        	System.out.println("Could not send event: " + e);
        }
    }
    public void updateConfidence(int newPercentage) {
        EiffelConfidenceLevelModifiedEvent event = EiffelConfidenceLevelModifiedEvent.Factory.create(null, null);
        event.setOptionalParameter("confidenceDonut", String.valueOf(newPercentage));
        send(event);
    }

    public void dispose() {
        if (sender != null)
            sender.dispose();
    }
}
