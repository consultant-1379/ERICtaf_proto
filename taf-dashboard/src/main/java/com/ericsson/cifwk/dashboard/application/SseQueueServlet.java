package com.ericsson.cifwk.dashboard.application;

import com.ericsson.cifwk.dashboard.data.EiffelMessageData;
import com.ericsson.cifwk.dashboard.util.QueueManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Singleton
public class SseQueueServlet extends EventSourceServlet {

    private static final Logger log = LoggerFactory.getLogger(SseQueueServlet.class);
    private final ObjectMapper objectMapper;
    private final QueueManager<EiffelMessageData> queueManager;

    @Inject
    public SseQueueServlet(ObjectMapper objectMapper,
                           QueueManager<EiffelMessageData> queueManager) {
        this.objectMapper = objectMapper;
        this.queueManager = queueManager;
    }

    @Override
    protected EventSource newEventSource(HttpServletRequest request) {
        return new SseSource(request.getRemoteAddr());
    }

    private class SseSource implements EventSource {

        private final String client;
        private final BlockingQueue<EiffelMessageData> queue;
        private volatile Emitter emitter;
        private volatile boolean active = true;

        public SseSource(String client) {
            this.client = client;
            this.queue = new LinkedBlockingQueue<>();
        }

        @Override
        public void onOpen(Emitter emitter) throws IOException {
            log.info("SSE opened at {}", client);
            this.emitter = emitter;
            queueManager.add(queue);
            eventLoop();
        }

        private void eventLoop() {
            try {
                while (active) {
                    EiffelMessageData data = queue.take();
                    processEvent(data);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void processEvent(EiffelMessageData data) {
            try {
                String json = objectMapper.writeValueAsString(data);
                emitter.data(json);
            } catch (IOException e) {
                log.error("Could not emit SSE data", e);
            }
        }

        @Override
        public void onClose() {
            log.info("SSE closed at {}", client);
            active = false;
            queueManager.remove(queue);
        }

    }

}
