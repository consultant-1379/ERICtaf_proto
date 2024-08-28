package com.ericsson.duraci.test.operators

import groovy.util.logging.Log4j

import com.ericsson.cifwk.taf.annotations.Context
import com.ericsson.cifwk.taf.annotations.Operator
import com.ericsson.cifwk.taf.data.DataHandler
import com.ericsson.cifwk.taf.data.Host
import com.ericsson.cifwk.taf.data.Ports
import com.ericsson.duraci.datawrappers.EventId
import com.ericsson.duraci.eiffelmessage.deserialization.Deserializer
import com.ericsson.duraci.eiffelmessage.deserialization.exceptions.MessageDeserializationException
import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent
import com.ericsson.duraci.eiffelmessage.messages.EiffelMessage
import com.ericsson.duraci.eiffelmessage.mmparser.MindMapParser
import com.ericsson.duraci.eiffelmessage.sending.MessageSender
import com.ericsson.duraci.test.config.PerformanceTestEiffelConfiguration
import com.ericsson.duraci.test.data.EventRepositoryTestDataProvider
import com.ericsson.duraci.test.jersey.JerseyClient
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.sun.jersey.api.client.ClientHandlerException


@Operator(context = Context.API)
@Log4j
public class EventRepositoryOperatorAPI implements EventRepositoryOperator {

    //get host info
    private Host host = DataHandler.getHostByName("eventrepo")
    private String portNo = host.getPort().get(Ports.HTTP)
    //check if rest call is requested
    private boolean makeRestCall = EventRepositoryTestDataProvider.getRestCallProperty()
    private boolean makeUpstreamRestCall = EventRepositoryTestDataProvider.getUpstreamRestCallProperty()
    private double restCallRate = EventRepositoryTestDataProvider.getRestCallRate()

    //get time delay properties
    double primaryDelay = EventRepositoryTestDataProvider.getDelayProperty("primaryDelay")
    double plusOrMinus = EventRepositoryTestDataProvider.getDelayProperty("plusOrMinus")
    Random rand = new Random();


    private PerformanceTestEiffelConfiguration config = new PerformanceTestEiffelConfiguration()

    @Override
    public boolean sendMessage(EiffelEvent event) {
        boolean result;
        MessageSender sender = new MessageSender.Factory(config).create();
        EiffelMessage eMsg = sender.send(event)
        log.debug "Sent message: ${eMsg.getEventId()}"
		sender.dispose();
        if (makeRestCall){
            result = checkRestCall(eMsg)
        }
        return result;
    }

    //create test flow
    @Override
    public List<Boolean> createLmUpFlow() {
        List<Boolean> results = new Vector<Boolean>();
        MindMapParser mmp = new MindMapParser("src/main/resources/data/test_flows", "LM_UP_Flow*.mm");

        Map<String, List<EiffelMessage>> mindMapMessages = mmp.getMessages(config);
        for (Map.Entry<String, List<EiffelMessage>> entry : mindMapMessages.entrySet()){
            List<EiffelMessage> messages = new ArrayList<EiffelMessage>()

            MessageSender sender = new MessageSender.Factory(config).create();
            for (EiffelMessage eMsg : entry.getValue()){
                sleep(getDelay())
                sender.send(eMsg)
                log.info("Sent message " + eMsg)
                messages.add(eMsg);
            }
			sender.dispose();
            //test rest call
            if (makeRestCall){
                for (Iterator<Boolean> iter = checkRestCall(messages).iterator(); iter.hasNext();){
                    results.add(iter.next())
                }
            }
            //test upstream event rest call
            if (makeUpstreamRestCall){
                for (Iterator<Boolean> iter = checkUpstreamRestCall(messages).iterator(); iter.hasNext();){
                    results.add(iter.next())
                }
            }

        }

        return results;
    }

	@Override
	public List<Boolean> createUserDefinedFlow() {
		List<Boolean> results = new Vector<Boolean>()
		MindMapParser mmp = new MindMapParser("src/main/resources/data/test_flows", "UserDefinedFlow*.mm");

		Map<String, List<EiffelMessage>> mindMapMessages = mmp.getMessages(config);
		for (Map.Entry<String, List<EiffelMessage>> entry : mindMapMessages.entrySet()){
			List<EiffelMessage> messages = new ArrayList<EiffelMessage>()

            MessageSender sender = new MessageSender.Factory(config).create();
			for (EiffelMessage eMsg : entry.getValue()){
				sleep(getDelay())
				sender.send(eMsg);
				log.info("Sent message " + eMsg)
				messages.add(eMsg);
			}
			sender.dispose();
			//test rest call
			if (makeRestCall){
				for (Iterator<Boolean> iter = checkRestCall(messages).iterator(); iter.hasNext();){
					results.add(iter.next())
				}
			}
			//test upstream event rest call
			if (makeUpstreamRestCall){
				for (Iterator<Boolean> iter = checkUpstreamRestCall(messages).iterator(); iter.hasNext();){
					results.add(iter.next())
				}
			}
		}
		return results
	}

    private int getDelay(){
        int delay = 0
        if (primaryDelay <= 0){
            delay = 0
        }
        else if (plusOrMinus <= 0 ){
            delay = (int)(1000 * primaryDelay)
        }
        else {
            delay = (1000 * primaryDelay) + (rand.nextInt((int)((1000 * primaryDelay) + 1)) - (1000 * plusOrMinus))
        }
        return delay
    }

    private List<Boolean> checkRestCall(List<EiffelMessage> messages){

        ArrayList<Boolean> results = new ArrayList<Boolean>()
        JerseyClient client;
        int okCode = 200;
        try{
            client =  new JerseyClient("http://${host.ip}:$portNo")
            okCode = client.getNormalResponseCode();
        }catch(ClientHandlerException e){
            log.error("Error:", e)
        }
        int responseCode
        int timeCount
        boolean check
        for (EiffelMessage m : selectRandomPercent(messages)){
            responseCode = 0
            timeCount = 0
            check = false
            while (timeCount < 5){
                log.info("Check rest call on: " + m.getEventId().toString())
                try{
                    responseCode = client.getEventResponseCode(m.getEventId().toString())
                }catch(ClientHandlerException e){
                    log.error("Error:", e)
                }

                if (responseCode == okCode){
                    check = true
                    log.info("Retrieved event via rest call: " + m.getEventId().toString())
                    break
                }
                sleep(200)
                timeCount++
            }
            if (!check){
                log.info("Failed to retrieve event via rest call: " + m.getEventId().toString())
            }
            results.add(check)
        }
        try{
            client.destroy()
        }catch (ClientHandlerException e){
            log.error("Error:", e)
        }

        return results
    }

    private boolean checkRestCall (EiffelMessage m){
        ArrayList<EiffelMessage> wrap = new ArrayList<EiffelMessage>()
        wrap.add(m)
        return checkRestCall(wrap).get(0)
    }

    private List<EiffelMessage> selectRandomPercent(List<EiffelMessage> messages){
        if (messages.size() == 1){
            return messages
        }
        else {
            ArrayList<EiffelMessage> randomList = new ArrayList<EiffelMessage>()
            ArrayList<Integer> alreadyPresent = new ArrayList<Integer>()
            int max = Math.round(messages.size * restCallRate)
            int current
            for(int i = 0; i < max; i++){
                while(true){
                    current = rand.nextInt(messages.size())
                    if (!alreadyPresent.contains(current)){
                        alreadyPresent.add(current)
                        randomList.add(messages.get(current))
                        break
                    }

                }
            }
            return randomList
        }
    }

    //returns JsonArray of JsonObjects & JsonArrays as a list of eiffelmessages
    private List<EiffelMessage> getAllUpstreamMessages(JsonArray messageArray) {
        List<EiffelMessage> retrievedUpstreamMessages = new ArrayList<EiffelMessage>()
        Deserializer deserializer = new Deserializer()
        for(Iterator<JsonElement> iter = messageArray.iterator(); iter.hasNext();) {
            JsonElement element = iter.next()
            if (element.isJsonObject()){
                try {

                    EiffelMessage msg = deserializer.deserialize(element)
                    //					log.debug "\n\nMessage retrieved from rest call: ${msg.getEventId()}\n"
                    retrievedUpstreamMessages.add(msg)
                }

                catch(MessageDeserializationException e){
                    log.error("Error:", e)
                }
            }
            else if (element.isJsonArray()){
                //				log.info("I have an array: ${element.toString()}")
                for (Iterator<EiffelMessage> msgIter = getAllUpstreamMessages(element).iterator() ; msgIter.hasNext() ; ) {
                    retrievedUpstreamMessages.add(msgIter.next())
                }
            }
        }
        return retrievedUpstreamMessages;
    }

    private List<EiffelMessage> getAllUpstreamMessages(String eventsString) {
        JsonParser parser = new JsonParser()
        try {
            return getAllUpstreamMessages(parser.parse(eventsString))
        }catch(JsonSyntaxException e){
            log.error("Error:", e)
        }
    }

    //returns chain of input event IDs
    private List<String> getInputEventIdChain(EiffelMessage msg, List<EiffelMessage> messages){

        List<String> upstreamEventIds = new ArrayList<String>()

        List<EventId> inputEventIds = msg.getInputEventIds()
        for (EventId eventId : inputEventIds){
            upstreamEventIds.add(eventId.toString())
            for (EiffelMessage m : messages){
                if (m.getEventId().toString().equals(eventId.toString())){
                    for(Iterator<String> iter = getInputEventIdChain(m, messages).iterator(); iter.hasNext();){
                        upstreamEventIds.add(iter.next())
                    }
                }
            }
        }
        return upstreamEventIds
    }
    //test upstream event rest call
    private List<Boolean> checkUpstreamRestCall(List<EiffelMessage> messages){

        List<Boolean> results = new ArrayList<Boolean>()
        JerseyClient client;
        try {
            client =  new JerseyClient("http://${host.ip}:$portNo")
        }catch(ClientHandlerException e){
            log.error("Error:", e)
        }
        for (EiffelMessage m : selectRandomPercent(messages)) {
            //log.debug "Rest Call for upstream events on message: ${m.getEventId().toString()}"
            String upstreamEvents = "status code: 404"
            int timeCount = 0
            while (timeCount < 5){
                log.info("Check upstream rest call on: " + m.getEventId().toString())
                try{
                    upstreamEvents = client.getUpstreamEvents(m.getEventId().toString());
                }catch(ClientHandlerException e){
                    log.error("Error:", e)
                }
                if (!upstreamEvents.toLowerCase().contains("status code: 404")){
                    log.info("Retrieved event via upstream rest call: " + m.getEventId().toString())
                    break
                }
                sleep(200)
                timeCount++
            }

            if (upstreamEvents.toLowerCase().contains("status code: 404")){
                log.info("Failed to retrieve event via upstream rest call: " + m.getEventId().toString())
                results.add(false)
            }
            else {
                //alter eiffelMessageVersion, this workaround is related to an issue with mongodb
                String updatedEvents = upstreamEvents.replaceAll("\"2\":", "\"2.1\":")

                //parse eiffelMessages from JsonArray string returned from rest call
                List<EiffelMessage> retrievedUpstreamMessages = getAllUpstreamMessages(updatedEvents)

                //get list of associated inputEventIds for current eiffelMessage
                List<String> inputEventIds = getInputEventIdChain(m, messages)

                if ((retrievedUpstreamMessages.size() -1) != inputEventIds.size()){
                    log.debug "Not all upstream events retrieved via rest call!"
                    results.add(false)
                }
                //compare list of inputEventIds received from upstream rest call and those generated
                //The upstream rest call always returns the current message plus the associated upstream events,
                //therefore, skipping the first array index
                for (int i = 1; i < retrievedUpstreamMessages.size(); i++){
                    results.add(retrievedUpstreamMessages.get(i).getEventId().toString().equals(inputEventIds.get(i-1)))
                }
            }
        }
        try{
            client.destroy()
        }catch (ClientHandlerException e){
            log.error("Error:", e)
        }
        return results
    }


}