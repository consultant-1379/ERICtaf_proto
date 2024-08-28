package com.ericsson.cifwk.dashboard.application;

import com.ericsson.cifwk.dashboard.data.EiffelMessageData;
import com.ericsson.cifwk.dashboard.util.CircularBuffer;
import com.ericsson.cifwk.dashboard.util.QueueManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SnapshotServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final QueueManager<EiffelMessageData> queueManager;
    private CircularBuffer<EiffelMessageData> buffer;

    @Inject
    public SnapshotServlet(ObjectMapper objectMapper,
                           QueueManager<EiffelMessageData> queueManager) {
        this.objectMapper = objectMapper;
        this.queueManager = queueManager;
    }

    @Override
    public void init() throws ServletException {
        super.init();

        int eventBufferSize = 500;
        String eventBufferSizeParam = getServletConfig().getInitParameter("eventBufferSize");
        if (eventBufferSizeParam != null)
            eventBufferSize = Integer.parseInt(eventBufferSizeParam);

        buffer = new CircularBuffer<>(eventBufferSize);
        queueManager.add(buffer);
    }

    @Override
    public void destroy() {
        queueManager.remove(buffer);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String json = objectMapper.writeValueAsString(buffer);
        response.setStatus(200);
        response.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        buffer.clear();
        response.setStatus(200);
    }

}
