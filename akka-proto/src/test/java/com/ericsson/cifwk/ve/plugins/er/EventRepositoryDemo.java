package com.ericsson.cifwk.ve.plugins.er;

import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import com.ericsson.cifwk.ve.infrastructure.config.SettingsProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;

import java.util.List;

public class EventRepositoryDemo {

    public static void main(String[] args) {
        SettingsProvider provider = new SettingsProvider();
        Settings settings = provider.loadSettings();

        EventRepository eventRepository = new EventRepository(
                settings.getString("eventRepository.uri"),
                new ObjectMapper(),
                new EiffelMessageService(settings.getString("eiffel.version"))
        );
        String topic = "eventData.jobExecutionId:Functional_Tests";
        DateTime from = DateTime.now().minusHours(1);
        String eventType = "EiffelJobStepStartedEvent";
        List<EiffelMessage> downstreams = eventRepository.findDownstreams(topic, from, eventType);
        System.out.println(downstreams.size());
    }

}
