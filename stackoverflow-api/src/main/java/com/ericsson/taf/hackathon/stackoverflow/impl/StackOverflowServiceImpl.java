package com.ericsson.taf.hackathon.stackoverflow.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.ericsson.taf.hackathon.stackoverflow.data.StackOverflowServiceException;
import com.ericsson.taf.hackathon.stackoverflow.intface.StackOverflowService;
import com.ericsson.taf.hackathon.stackoverflow.response.ErrorResponse;
import com.ericsson.taf.hackathon.stackoverflow.response.LoginResponse;
import com.ericsson.taf.hackathon.stackoverflow.utils.XMLHandler;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import javax.ws.rs.core.MediaType;

//class includes  main to test the program
public class StackOverflowServiceImpl implements StackOverflowService {

    Client client = ClientHelper.createClient();

    private String URL = "http://atvcloud2.athtem.eei.ericsson.se:9010";

    private String username;

    private String password;

    private static final Logger LOGGER = Logger.getLogger(StackOverflowServiceImpl.class.getName());

    public StackOverflowServiceImpl(){}

    public StackOverflowServiceImpl(String url, String username, String password) {
        this.URL = url;
        this.username = username;
        this.password = password;
    }

    private static String getDuration(long start, long end) {
        return String.format("%.2f", ((double) (end - start) / 1000)) + " seconds";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getServer() {
        return this.URL;
    }

    @Override
    public String login() throws StackOverflowServiceException {
        String className = getClass().getSimpleName();
        String functionName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String restDetail = "REST Call (Login Test) ";

        client.addFilter(new HTTPBasicAuthFilter(username, password));

        String login = null;
        try {
            String requestURL = URL + "/logintest.xml";

            long startTime = System.currentTimeMillis();
            WebResource webResource = client.resource(requestURL);
            ClientResponse clientResponse = webResource.get(ClientResponse.class);
            clientResponse.bufferEntity();
            long endTime = System.currentTimeMillis();

            try {
                if (clientResponse.getStatus() == 200) {
                    LoginResponse responseText = clientResponse.getEntity(LoginResponse.class);
                    login = responseText.getLoginstatus();
                } else {
                    ErrorResponse errorResp = clientResponse.getEntity(ErrorResponse.class);
                    throw new StackOverflowServiceException(errorResp.getError());
                }
            } catch (StackOverflowServiceException e) {
                throw new StackOverflowServiceException(e.getMessage());
            } catch (Exception e) {
                clientResponse.getEntityInputStream().reset();
                String entity = clientResponse.getEntity(String.class);
                throw new StackOverflowServiceException(entity);
            } finally {
                clientResponse.close();
            }
        } catch (Exception e) {
            throw new StackOverflowServiceException(e.getMessage());
        }

        return login;
    }

    @Override
    public String getAnswer(String query) {
        String className = getClass().getSimpleName();
        String functionName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String args = query;

        String datacenterId = "";

        client.addFilter(new HTTPBasicAuthFilter(username, password));
        try {
            String requestURL = URL + "//.xml";

            long startTime = System.currentTimeMillis();
            AsyncWebResource ayncWebResource = client.asyncResource(requestURL);
            final Future<ClientResponse> asyncClientResponse = ayncWebResource.get(ClientResponse.class);
            ClientResponse clientResponse = asyncClientResponse.get(10, TimeUnit.MINUTES);
            clientResponse.bufferEntity();
            long endTime = System.currentTimeMillis();

            try {
                if (clientResponse.getStatus() == 200) {

                    InputStream inputStream = clientResponse.getEntityInputStream();
                    BufferedReader outputStream = new BufferedReader(new InputStreamReader(inputStream));
                    String xmlString = "";
                    try {
                        for (String line = outputStream.readLine(); line != null; line = outputStream.readLine()) {
                            xmlString += line;
                        }
                    } catch (IOException e) {

                    } finally {
                        inputStream.close();
                        outputStream.close();
                    }
                    try {
                        Document xmlDocument = XMLHandler.createDocument(xmlString);

                    } catch (Exception error) {

                    }

                } else {
                    ErrorResponse errorResp = clientResponse.getEntity(ErrorResponse.class);
                    throw new StackOverflowServiceException(errorResp.getError());
                }
            } catch (StackOverflowServiceException e) {
                throw new StackOverflowServiceException(e.getMessage());
            } catch (Exception e) {
                clientResponse.getEntityInputStream().reset();
                String entity = clientResponse.getEntity(String.class);
                throw new StackOverflowServiceException(entity);
            } finally {
                clientResponse.close();
            }
        } catch (Exception e) {

        }
        return datacenterId;
    }

    public String niall(){
        Client client = Client.create();

        WebResource webResource = client

                .resource("http://atvts789.athtem.eei.ericsson.se/hackathon/response");

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)

                .get(ClientResponse.class);

        if (response.getStatus() != 200) {

            throw new RuntimeException("Failed : HTTP error code : "

                    + response.getStatus());

        }
        String output = response.getEntity(String.class);
        System.out.println("Output from Server .... \n");
        System.out.println(response.getLocation());
        System.out.println(output);

        return output;
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public List<LogObject> jasonParser(String query) throws IOException {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(niall());
        Gson gson = new Gson();

        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> myMap = gson.fromJson(element, type);

        List<LogObject> lo = new ArrayList<LogObject>();
        String tempTitle = "";
        for (Map<String, String> itemObject : myMap) {
            for (Map.Entry<String, String> items : itemObject.entrySet()) {
                if (items.getKey().equals("title")) {
                    tempTitle = items.getValue();
                } else {
                    if (tempTitle.contains(query)) {
                        lo.add(new LogObject(tempTitle, items.getValue()));
                    }
                }
            }
        }
        return lo;
    }

    public static void main(String... strings) {
        StackOverflowServiceImpl soim = new StackOverflowServiceImpl();
        try {
            soim.jasonParser("Too many open files");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Testing");
    }

}
