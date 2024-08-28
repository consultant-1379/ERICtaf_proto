package com.ericsson.duraci.test.jersey;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class JerseyClient {
	private Client client;
    private WebResource webResource;
    private String baseURI;
    String ER_URI = "/eventrepo/restapi/events";

    public JerseyClient (String uri){
    	baseURI = uri + ER_URI;
        client = Client.create();
        webResource = client.resource(uri);
    }
    public String getEvent(String messageId){
    	webResource = client.resource(baseURI + "/" + messageId);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        return response.getEntity(String.class);
    }
    public int getEventResponseCode(String messageId){
    	webResource = client.resource(baseURI + "/" + messageId);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        return response.getStatus();
    }
    public String getUpstreamEvents(String messageId){
    	webResource = client.resource(baseURI + "/" + messageId + "/upstream");
    	ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        return response.getEntity(String.class);
    }
    
    public int getNormalResponseCode(){
    	return ClientResponse.Status.OK.getStatusCode();
    }
    
    public void destroy(){
    	client.destroy();
    }

}
