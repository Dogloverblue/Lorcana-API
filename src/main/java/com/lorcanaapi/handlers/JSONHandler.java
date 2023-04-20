package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class JSONHandler implements HttpHandler {
	int JSONRequestCount = 0;
	String response;
    @Override
    public void handle(HttpExchange t) throws IOException {
	System.out.println("Json response made; it was Json request number " + JSONRequestCount );
	JSONRequestCount++;
    t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        t.sendResponseHeaders(200, response.length());
  
        OutputStream os = t.getResponseBody();
        
        os.write(response.getBytes());
        os.close();
    }
    public JSONHandler(String response) {
    	this.response = response;
    }
}