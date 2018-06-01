package com.alimgiray.bdd.core.subject;

import com.alimgiray.bdd.core.message.SoapMessage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author aaytar
 * @since 31.05.2018 16:11
 */
public class SoapClient {

    private String endpointUrl;
    private String response;

    public SoapClient(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getResponse() {
        return response;
    }

    public void send(SoapMessage soapMessage) throws IOException {

        HttpURLConnection connection = getConnection();
        sendMessage(soapMessage, connection);
        this.response = getResponse(connection);
    }

    private String getResponse(HttpURLConnection connection) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
        System.out.println(response.toString());
        return response.toString();
    }

    private HttpURLConnection getConnection() throws IOException {

        HttpURLConnection connection;

        URL url = new URL(endpointUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        return connection;

    }

    private void sendMessage(SoapMessage soapMessage, HttpURLConnection connection) throws IOException {
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(soapMessage.toString());
        wr.close();
    }

}
