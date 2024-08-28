package com.ericsson.cifwk.taf.grid.sample;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 */
public final class RestTestCase {

    private final String URI = "http://e7b499bae43baf:9090/test";

    public static void main(String[] args) throws Exception {
        RestTestCase testCase = new RestTestCase();
        testCase.addition();
    }

    @Test
    public void addition() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(URI);

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            if (200 != response.getStatusLine().getStatusCode()) {
                throw new IllegalArgumentException("");
            }
        }
    }

}
