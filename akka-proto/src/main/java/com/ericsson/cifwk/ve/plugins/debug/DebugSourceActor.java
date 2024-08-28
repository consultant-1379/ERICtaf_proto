package com.ericsson.cifwk.ve.plugins.debug;

import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.UntypedActor;
import com.ericsson.cifwk.ve.actor.message.DashboardPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.MessageConsumer;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;
import com.ericsson.cifwk.ve.application.routing.MessageRouter;
import com.ericsson.cifwk.ve.web.DashboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterators;
import com.google.common.io.Resources;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DebugSourceActor extends UntypedActor {

    public static final TypeReference<List<EiffelMessageWrapper>> WRAPPER_LIST_TYPE =
            new TypeReference<List<EiffelMessageWrapper>>() {
            };

    private final String job;

    public DebugSourceActor(String job) {
        this.job = job;
    }

    @Override
    public void preStart() throws Exception {
        final Iterator<String> messages = getMessageIterator(loadDebugMessages(job));
        ActorSystem system = context().system();
        Scheduler scheduler = system.scheduler();
        scheduler.schedule(Duration.Zero(), Duration.create(1, TimeUnit.SECONDS), new Runnable() {
            @Override
            public void run() {
                String json = messages.next();
                self().tell(new DashboardPublish(json), self());
            }
        }, system.dispatcher());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DashboardPublish) {
            publish((DashboardPublish) message);
        } else if (message instanceof DashboardSubscribe) {
            subscribe((DashboardSubscribe) message);
        } else {
            unhandled(message);
        }
    }

    private void subscribe(DashboardSubscribe message) throws IOException {
        final DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        final EiffelMessageService eiffelMessageService = Bootstrap.getInstance().getEiffelMessageService();
        final MessageRouter router = Bootstrap.getInstance().getMessageRouter();
        final String sessionId = message.getSessionId();
        final String topic = message.getTopic();

        List<EiffelMessageWrapper> wrappers = loadDebugMessages(job);
        for (EiffelMessageWrapper wrapper : wrappers) {
            EiffelMessage event = eiffelMessageService.unwrap(wrapper);
            router.getRouterResponse(event, topic)
                    .serialize(new MessageConsumer() {
                        @Override
                        public void consume(String json) {
                            dashboardService.directPublish(sessionId, json);
                        }
                    });
        }
    }

    private void publish(DashboardPublish message) {
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        String json = message.getJson();
        dashboardService.publish(json);
    }

    private static List<EiffelMessageWrapper> loadDebugMessages(String job) throws IOException {
        ObjectMapper objectMapper = Bootstrap.getInstance().getObjectMapper();
        return loadDebugMessages(job, objectMapper);
    }

    static List<EiffelMessageWrapper> loadDebugMessages(String job,
                                                        final ObjectMapper objectMapper)
            throws IOException {
        URL filesResource = Resources.getResource("debug/eiffel/" + job + ".json");
        String json = Resources.toString(filesResource, Charsets.UTF_8);
        return objectMapper.readValue(json, WRAPPER_LIST_TYPE);
    }

    private static Iterator<String> getMessageIterator(List<EiffelMessageWrapper> messageWrappers) {
        ObjectMapper objectMapper = Bootstrap.getInstance().getObjectMapper();
        EiffelMessageService eiffelMessageService = Bootstrap.getInstance().getEiffelMessageService();
        return getMessageIterator(messageWrappers, objectMapper, eiffelMessageService);
    }

    static Iterator<String> getMessageIterator(List<EiffelMessageWrapper> messageWrappers,
                                               final ObjectMapper objectMapper,
                                               final EiffelMessageService eiffelMessageService) {
        SampleMessageIterator messageIterator =
                new SampleMessageIterator(messageWrappers, eiffelMessageService);
        return Iterators.transform(messageIterator, new Function<EiffelMessageWrapper, String>() {
            @Override
            public String apply(EiffelMessageWrapper messageWrapper) {
                try {
                    return objectMapper.writeValueAsString(messageWrapper);
                } catch (JsonProcessingException e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

    private static class SampleMessageIterator implements Iterator<EiffelMessageWrapper> {

        private final DateTimeFormatter iso8601;
        private final EiffelMessageService eiffelMessageService;
        private Collection<EiffelMessageWrapper> messageWrappers;
        private Iterator<EiffelMessageWrapper> messagesIterator;

        public SampleMessageIterator(Collection<EiffelMessageWrapper> messageWrappers,
                                     EiffelMessageService eiffelMessageService) {
            this.messageWrappers = messageWrappers;
            this.eiffelMessageService = eiffelMessageService;
            iso8601 = ISODateTimeFormat.dateTime().withZoneUTC();
            messagesIterator = setupIterator();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public synchronized EiffelMessageWrapper next() {
            if (!messagesIterator.hasNext()) {
                messagesIterator = setupIterator();
            }
            return messagesIterator.next();
        }

        private Iterator<EiffelMessageWrapper> setupIterator() {
            Map<String, String> ids = new HashMap<>(messageWrappers.size());
            for (EiffelMessageWrapper messageWrapper : messageWrappers) {
                EiffelMessage message = eiffelMessageService.unwrap(messageWrapper);
                String oldId = eiffelMessageService.getId(message);
                String newId = newEventId();
                ids.put(oldId, newId);
            }
            Collection<EiffelMessageWrapper> newMessageWrappers = new ArrayList<>();
            for (EiffelMessageWrapper messageWrapper : messageWrappers) {
                EiffelMessage message = eiffelMessageService.unwrap(messageWrapper);

                String eventId = eiffelMessageService.getId(message);
                eiffelMessageService.setId(message, ids.get(eventId));
                String parentEventId = eiffelMessageService.getParentId(message);
                eiffelMessageService.setParentId(message, ids.get(parentEventId));
                message.setEventTime(iso8601.print(DateTime.now()));

                EiffelMessageWrapper newMessageWrapper = eiffelMessageService.wrap(message);
                newMessageWrappers.add(newMessageWrapper);
            }
            messageWrappers = newMessageWrappers;
            return messageWrappers.iterator();
        }

        private static String newEventId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
