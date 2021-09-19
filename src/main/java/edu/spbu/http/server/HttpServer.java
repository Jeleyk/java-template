package edu.spbu.http.server;

import java.net.InetSocketAddress;

public class HttpServer {

    public static void main(String[] args) throws Exception {
        com.sun.net.httpserver.HttpServer server =
                com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHttpHandler() {{
            addReplacementParameter("name", s -> ", " + s + "!");
        }});
        server.setExecutor(null);
        server.start();
    }

}
