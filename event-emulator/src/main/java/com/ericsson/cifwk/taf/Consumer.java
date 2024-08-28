package com.ericsson.cifwk.taf;

import com.ericsson.duraci.datawrappers.MessageBus;
import com.ericsson.duraci.eiffelmessage.binding.MessageBusBinder;
import com.ericsson.duraci.eiffelmessage.binding.MessageBusBindings;
import com.ericsson.duraci.eiffelmessage.binding.MessageConsumer;
import com.ericsson.duraci.eiffelmessage.binding.configuration.BindingConfiguration;
import com.ericsson.duraci.eiffelmessage.binding.exceptions.EiffelMessageConsumptionException;
import com.ericsson.duraci.eiffelmessage.messages.EiffelMessage;

/**
 *
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        final MessageBusBindings.Factory messageBusBindingsFactory = new MessageBusBindings.Factory();
        MessageBus messageBus = new MessageBus(Settings.HOST_NAME, Settings.EXCHANGE, "EventRepository");
        final MessageBusBinder binder = messageBusBindingsFactory.create(messageBus, Settings.DOMAIN);
        binder.add(new BindingConfiguration(false, new MessageConsumer() {
            @Override
            public void consumeMessage(EiffelMessage message) throws EiffelMessageConsumptionException {
                System.out.println(message.getEventType());
            }

            @Override
            public String getName() {
                return "Hello";
            }
        }, "#"));

    }

}
