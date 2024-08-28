package com.ericsson.duraci.eiffelmessage.messages.events.testing;

import com.ericsson.duraci.datawrappers.EventId;
import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.datawrappers.Request;
import com.ericsson.duraci.datawrappers.Response;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.virtual.EiffelSubEvent;
import com.ericsson.duraci.eiffelmessage.messages.v3.events.TestSampleEventImpl;

public interface TestSampleEvent extends EiffelSubEvent {

    Long getSampleId();

    String getProtocol();

    Request getRequest();

    Response getResponse();

    ExecutionId getTestCaseExecutionId();
    
    public static class Factory {
        private Factory() {
        }

        public static TestSampleEvent create(EventId eventId,
        									EventId parentId,
        									ExecutionId executionId,
        									ExecutionId testCaseExecutionId,
                                             Request request,
                                             Response response,
                                             String protocol,
                                             Long sampleId) {
            return new TestSampleEventImpl(eventId, parentId, executionId, testCaseExecutionId, response, request, protocol, sampleId);
        }

    }

}
