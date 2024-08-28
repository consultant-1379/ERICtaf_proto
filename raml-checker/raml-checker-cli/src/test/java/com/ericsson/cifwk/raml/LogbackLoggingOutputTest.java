package com.ericsson.cifwk.raml;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public abstract class LogbackLoggingOutputTest {

    @Mock
    private Appender<ILoggingEvent> appender;

    @Captor
    private ArgumentCaptor<LoggingEvent> eventCaptor;

    @Before
    public void setUp() throws Exception {
        getRootLogger().addAppender(appender);
    }

    @After
    public void tearDown() throws Exception {
        getRootLogger().detachAppender(appender);
    }

    private Logger getRootLogger() {
        return (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    protected void verifyLogMessages(Level level, String... messages) {
        Object[] levelMessages = new Object[messages.length * 2];
        for (int i = 0; i < messages.length; i++) {
            levelMessages[i * 2] = level;
            levelMessages[i * 2 + 1] = messages[i];
        }
        verifyLogMessages(levelMessages);
    }

    protected void verifyLogMessages(Object... levelMessages) {
        checkArgument(levelMessages.length % 2 == 0, "Expected input: LEVEL, MESSAGE, [LEVEL, MESSAGE]...");
        verify(appender, times(levelMessages.length / 2)).doAppend(eventCaptor.capture());
        List<LoggingEvent> events = eventCaptor.getAllValues();
        for (int i = 0; i < events.size(); i++) {
            LoggingEvent event = events.get(i);
            assertThat(event.getLevel()).isSameAs(levelMessages[i * 2]);
            assertThat(event.getFormattedMessage()).isEqualTo(levelMessages[i * 2 + 1]);
        }
    }
}
