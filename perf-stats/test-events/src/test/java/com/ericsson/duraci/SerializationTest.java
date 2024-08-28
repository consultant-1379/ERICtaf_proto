package com.ericsson.duraci;

import org.junit.Test;

import com.ericsson.duraci.datawrappers.EventId;
import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.datawrappers.Request;
import com.ericsson.duraci.datawrappers.Response;
import com.ericsson.duraci.datawrappers.ResultCode;
import com.ericsson.duraci.eiffelmessage.messages.EiffelMessage;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.TestSampleEvent;
import com.ericsson.duraci.eiffelmessage.messages.v2.EiffelMessageImpl;
import com.ericsson.duraci.eiffelmessage.serialization.Serializer;
import com.ericsson.duraci.eiffelmessage.serialization.printing.Printer;

public class SerializationTest {

    @Test
    public void serializeToJson() throws Exception {
        EventId eventId = new EventId();
        ExecutionId executionId = new ExecutionId();
        ExecutionId testCaseExecutionId = new ExecutionId();

        Request request = new Request();
        request.setData("http://blob.repo.com");
        request.setTarget("http://system");
        request.setSize(1000);
        request.setType("GET");

        Response response = new Response();
        response.setData("h");
        response.setResult(ResultCode.SUCCESS);
        response.setSize(1024);
        response.setTime(555);
        response.setType("200");

        TestSampleEvent event = TestSampleEvent.Factory.create(eventId, null, executionId, testCaseExecutionId, request, response, "GET", 1000L);

        event.setOptionalParameter("thread", Thread.currentThread().getName());
        event.setOptionalParameter("latency", "100");
        EiffelMessage message = new EiffelMessageImpl("domainId", event);

        Serializer serializer = new Serializer();
        Printer printer = serializer.pretty(message);

        System.out.println(printer.print());
    }

}
