package edu.spbu.http.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyHttpHandler implements HttpHandler {

    private HashMap<String, Function<String, String>> replacementParameters = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HashMap<String, String> requestParamValue = null;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }

        handleResponse(httpExchange, requestParamValue);
    }

    private HashMap<String, String> handleGetRequest(HttpExchange httpExchange) {
        HashMap<String, String> parameters = new HashMap<>();
        for (String s : httpExchange.getRequestURI().toString().split("\\?")) {
            String[] split = s.split("=");
            if (split.length < 2) continue;
            parameters.put(split[0], split[1]);
        }
        return parameters;
    }


    private void handleResponse(HttpExchange httpExchange, HashMap<String, String> parameters) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        BufferedReader reader = new BufferedReader(new FileReader("html/test.html"));
        StringBuilder htmlBuilder = new StringBuilder();
        while (reader.ready()) {
            htmlBuilder.append(reader.readLine());
        }
        reader.close();
        htmlBuilder = replaceParameters(htmlBuilder, parameters);

        httpExchange.sendResponseHeaders(200, htmlBuilder.length());

        outputStream.write(htmlBuilder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    protected void addReplacementParameter(String key, Function<String, String> function) {
        replacementParameters.put(key, function);
    }

    private StringBuilder replaceParameters(StringBuilder builder, HashMap<String, String> parameters) {
        StringBuilder builder1 = new StringBuilder();
        for (Map.Entry<String, Function<String, String>> entry : replacementParameters.entrySet()) {
            Pattern p = Pattern.compile("%" + entry.getKey() + "%");
            Matcher m = p.matcher(builder.toString());
            if (parameters.containsKey(entry.getKey())) {
                while (m.find()) {
                    m.appendReplacement(builder1, entry.getValue().apply(parameters.get(entry.getKey())));
                }
            } else {
                while (m.find()) {
                    m.appendReplacement(builder1, "");
                }
            }
            m.appendTail(builder1);
        }
        return builder1;
    }

}