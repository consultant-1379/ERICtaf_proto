package com.ericsson.cifwk.ve.plugins.debug;

import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import com.ericsson.cifwk.ve.infrastructure.config.SettingsProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DebugSourceActorTest {

    private String job;
    private EiffelMessageService ems;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        SettingsProvider provider = new SettingsProvider();
        Settings settings = provider.loadSettings();
        String eiffelVersion = settings.getString("eiffel.version");
        job = settings.getString("debug.job");
        ems = new EiffelMessageService(eiffelVersion);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldLoadDebugMessages() throws Exception {
        List<EiffelMessageWrapper> wrappers = DebugSourceActor.loadDebugMessages(job, objectMapper);

        assertTrue(wrappers.size() > 0);
    }

    @Test
    public void shouldIterateRepeatedly() throws Exception {
        List<EiffelMessageWrapper> wrappers = DebugSourceActor.loadDebugMessages(job, objectMapper);
        int messageCount = wrappers.size();
        Iterator<String> iterator =
                DebugSourceActor.getMessageIterator(wrappers, objectMapper, ems);
        EiffelMessage first = parseMessage(iterator.next());
        Iterators.advance(iterator, messageCount - 1);
        EiffelMessage nextFirst = parseMessage(iterator.next());

        assertFalse(ems.getId(first).equals(ems.getId(nextFirst)));
        assertTrue(first.getEventType().equals("EiffelJobStepStartedEvent"));
        assertTrue(first.getEventType().equals(nextFirst.getEventType()));
    }

    private EiffelMessage parseMessage(String json) throws IOException {
        EiffelMessageWrapper messageWrapper = objectMapper.readValue(json, EiffelMessageWrapper.class);
        return ems.unwrap(messageWrapper);
    }

}
