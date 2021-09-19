package edu.spbu.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        httpClient.sendAndPrintGetRequest("http://localhost:8000/test?name=Jeleyka");
    }

    public void sendAndPrintGetRequest(String request) {
        try {
            URLConnection urlConnection = new URL(request).openConnection();
            urlConnection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while (reader.ready()) {
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
