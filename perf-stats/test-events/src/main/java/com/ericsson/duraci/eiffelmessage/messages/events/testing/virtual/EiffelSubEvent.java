package com.ericsson.duraci.eiffelmessage.messages.events.testing.virtual;

import com.ericsson.duraci.datawrappers.EventId;
import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;

public interface EiffelSubEvent extends EiffelEvent {

    EventId getEventId();

    EventId getParentId();

    ExecutionId getExecutionId();
}
