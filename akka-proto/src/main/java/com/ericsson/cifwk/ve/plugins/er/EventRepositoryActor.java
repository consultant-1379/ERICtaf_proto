package com.ericsson.cifwk.ve.plugins.er;

import akka.actor.UntypedActor;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;
import com.ericsson.cifwk.ve.web.DashboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;

import java.util.List;

public class EventRepositoryActor extends UntypedActor {

    private final EventRepository eventRepository;
    private final int minutes;
    private final String eventType;

    public EventRepositoryActor(String uri, int minutes, String eventType) {
        eventRepository = new EventRepository(uri);
        this.minutes = minutes;
        this.eventType = eventType;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DashboardSubscribe) {
            restore((DashboardSubscribe) message);
        } else {
            unhandled(message);
        }
    }

    private void restore(DashboardSubscribe message) {
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        ObjectMapper objectMapper = Bootstrap.getInstance().getObjectMapper();
        EiffelMessageService eiffelMessageService = Bootstrap.getInstance().getEiffelMessageService();

        DateTime from = DateTime.now().minusMinutes(minutes);
        List<EiffelMessage> events = eventRepository.findEvents(from); // FIXME Needs to be by topic or downstream
        for (EiffelMessage event : events) {
            // FIXME Needs to be direct publish
            try {
                EiffelMessageWrapper wrapper = eiffelMessageService.wrap(event);
                String json = objectMapper.writeValueAsString(wrapper);
                dashboardService.publish(json);
            } catch (JsonProcessingException ignored) {
            }
        }
    }

}
