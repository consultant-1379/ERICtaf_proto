package com.ericsson.duraci.eiffelmessage.messages.v3.events;

import com.ericsson.duraci.datawrappers.EventId;
import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.datawrappers.Request;
import com.ericsson.duraci.datawrappers.Response;
import com.ericsson.duraci.eiffelmessage.messages.Event;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.TestSampleEvent;
import com.ericsson.duraci.eiffelmessage.messages.v3.events.virtual.GenericEiffelEventFieldsImpl;

@Event(eventType="TestSampleEvent")
public class TestSampleEventImpl extends GenericEiffelEventFieldsImpl implements TestSampleEvent {

    private Long sampleId;
    private EventId eventId;
    private EventId parentId;
    private ExecutionId executionId;
    private ExecutionId testCaseExecutionId;
    private String protocol;
    private Request request;
    private Response response;

    public TestSampleEventImpl(TestSampleEvent source) {
        this(source.getEventId(), source.getParentId(), source.getExecutionId(), source.getTestCaseExecutionId(), source.getResponse(), 
        		source.getRequest(), source.getProtocol(), source.getSampleId());
        setGenericFields(source);
    }

    public TestSampleEventImpl(EventId eventId, EventId parentId, ExecutionId executionId, ExecutionId testCaseExecutionId, Response response, 
    		Request request, String protocol, Long sampleId) {
        this.eventId = eventId;
        this.executionId = executionId;
        this.testCaseExecutionId = testCaseExecutionId;
        this.response = response;
        this.request = request;
        this.protocol = protocol;
        this.sampleId = sampleId;
    }

    @Override
    public Long getSampleId() {
        return sampleId;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public EventId getEventId() {
        return eventId;
    }

    @Override
    public EventId getParentId() {
        return parentId;
    }

    @Override
    public String getFamilyRoutingKeyWord() {
        return "testing";
    }

    @Override
    public String getTypeRoutingKeyWord() {
        return "sample";
    }

	@Override
	public ExecutionId getExecutionId() {
		return executionId;
	}
	
	@Override
	public ExecutionId getTestCaseExecutionId() {
		return testCaseExecutionId;
	}
}
