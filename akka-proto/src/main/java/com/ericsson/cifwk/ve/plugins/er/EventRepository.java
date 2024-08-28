package com.ericsson.cifwk.ve.plugins.er;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventRepository {

    private final String base;
    private final ObjectMapper objectMapper;
    private final EiffelMessageService eiffelMessageService;
    private final DateTimeFormatter iso8601;
    private final Client client;
    private final CollectionType responseType;

    public EventRepository(String uri) {
        this(
                uri,
                Bootstrap.getInstance().getObjectMapper(),
                Bootstrap.getInstance().getEiffelMessageService()
        );
    }

    EventRepository(
            String uri,
            ObjectMapper objectMapper,
            EiffelMessageService eiffelMessageService) {
        this.base = uri + "/events/";
        this.objectMapper = objectMapper;
        this.eiffelMessageService = eiffelMessageService;
        iso8601 = ISODateTimeFormat.dateTime().withZoneUTC();
        client = ClientBuilder.newClient();
        responseType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, EiffelMessageWrapper.class);
    }

    public List<EiffelMessage> findEvents(DateTime from) {
        String query = EventRepositoryQuery.create()
                .greaterThanOrEqual("eventTime", iso8601.print(from))
                .build();
        return requestEvents(queryUri(query));
    }

    public List<EiffelMessage> findEvents(String topic, DateTime from) {
        String query = EventRepositoryQuery.fromTopic(topic)
                .greaterThanOrEqual("eventTime", iso8601.print(from))
                .build();
        return requestEvents(queryUri(query));
    }

    /**
     * Finds events and their downstream by given topic
     * <p/>
     * Warning! Makes an HTTP request for each found parent.
     *
     * @param topic     dashboard topic
     * @param from      starting time in the past
     * @param eventType Eiffel event type
     * @return events and downstream
     */
    public List<EiffelMessage> findDownstreams(String topic, DateTime from, String eventType) {
        String query = EventRepositoryQuery.fromTopic(topic)
                .equals("eventType", eventType)
                .greaterThanOrEqual("eventTime", iso8601.print(from))
                .build();
        List<EiffelMessage> parentEvents = requestEvents(queryUri(query));
        List<EiffelMessage> downstreamEvents = new ArrayList<>();
        for (EiffelMessage parentEvent : parentEvents) {
            String eventId = parentEvent.getEventId();
            List<EiffelMessage> events = requestEvents(downstreamUri(eventId));
            downstreamEvents.addAll(events);
        }
        return downstreamEvents;
    }

    private String downstreamUri(String eventId) {
        return base + eventId + "/downstream/";
    }

    private String queryUri(String query) {
        return base + "?" + query;
    }

    private List<EiffelMessage> requestEvents(String uri) {
        Invocation.Builder request = client.target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE);
        Response response = request.get();
        String json = response.readEntity(String.class);
        List<EiffelMessageWrapper> wrappers = parseEvents(json);
        List<EiffelMessage> events = new ArrayList<>(wrappers.size());
        for (EiffelMessageWrapper wrapper : wrappers) {
            EiffelMessage event = eiffelMessageService.unwrap(wrapper);
            events.add(event);
        }
        return events;
    }

    private List<EiffelMessageWrapper> parseEvents(String json) {
        try {
            return objectMapper.readValue(json, responseType);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
