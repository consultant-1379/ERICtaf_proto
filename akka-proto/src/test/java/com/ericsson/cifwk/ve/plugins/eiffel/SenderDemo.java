package com.ericsson.cifwk.ve.plugins.eiffel;

import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import com.ericsson.cifwk.ve.infrastructure.config.SettingsProvider;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelJobStartedEvent;
import com.ericsson.duraci.eiffelmessage.mmparser.clitool.EiffelConfig;

/**
 *
 */
public class SenderDemo {

    public static void main(String[] args) {
        SettingsProvider provider = new SettingsProvider();
        Settings settings = provider.loadSettings();

        String host = settings.getString("amqp.host");
        String exchangeName = settings.getString("amqp.exchangeName");

        EiffelConfig configuration = new EiffelConfig("test.execution", exchangeName, host);
        Sender sender = new Sender(configuration);
        sender.init();

        sender.send(EiffelJobStartedEvent.Factory.create("test", "123", 5));

        sender.shutdown();
    }

}
