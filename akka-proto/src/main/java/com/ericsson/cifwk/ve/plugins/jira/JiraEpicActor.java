package com.ericsson.cifwk.ve.plugins.jira;

import akka.actor.UntypedActor;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.MessageConsumer;
import com.ericsson.cifwk.ve.plugins.jira.dto.JiraRequest;
import com.ericsson.cifwk.ve.plugins.jira.dto.JiraResponse;
import com.ericsson.cifwk.ve.web.DashboardService;
import com.ericsson.cifwk.ve.web.ServerResponseSerializer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class JiraEpicActor extends UntypedActor {

    public static final String SENDER_NAME = "jira";

    final Logger logger = LoggerFactory.getLogger(JiraEpicActor.class.getName());

    private LoadingCache<String, JiraResponse> jiraCache;
    private Jira jira;

    public JiraEpicActor(String username, String password, String uri, String storyPointsField) {
        this.jira = new Jira(username, password, uri, storyPointsField);
    }

    @Override
    public void preStart() throws Exception {
        logger.info("Starting JiraActor");
        jira.connect();
        jiraCache = CacheBuilder.newBuilder()
                .maximumSize(20)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, JiraResponse>() {
                    public JiraResponse load(String epicId) throws Exception {
                        return jira.getEpicStructure(epicId);
                    }
                });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof JiraRequest) {
            request((JiraRequest) message);
        } else {
            unhandled(message);
        }
    }

    private void request(JiraRequest message) throws ExecutionException {
        logger.info("request for jira data : " + message);
        final String sessionId = message.getSessionId();
        final String epicId = message.getEpicId();
        final DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();

        JiraResponse response = jiraCache.get(epicId);
        ServerResponseSerializer.create(SENDER_NAME)
                .message(response)
                .data("epicId", epicId)
                .serialize(new MessageConsumer() {
                    @Override
                    public void consume(String json) {
                        dashboardService.directPublish(sessionId, json);
                    }
                });
    }

}
