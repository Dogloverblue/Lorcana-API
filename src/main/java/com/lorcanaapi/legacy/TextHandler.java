package com.lorcanaapi.legacy;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TextHandler implements HttpHandler {
			static int TextRequestCount = 0;
		    String response;
	        @Override
	        public void handle(HttpExchange t) throws IOException {
//			System.out.println("Text response made; it was text request number " + TextRequestCount);
			TextRequestCount++;
			TrackingHandler.TextRequestCount++;
				t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
	            t.sendResponseHeaders(200, response.length());
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
	        }
		    public TextHandler(String response) {
			    this.response = response;
		    }
	}


