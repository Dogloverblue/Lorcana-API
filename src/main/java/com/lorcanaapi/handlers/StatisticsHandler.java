package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StatisticsHandler implements HttpHandler {
	
	
	static Map<String, Integer> handlerRequestCount = new HashMap<>();
	
	public static void incrementHandlerCount(String handlerName) {
		if (handlerRequestCount.containsKey(handlerName)) {
			handlerRequestCount.put(handlerName, handlerRequestCount.get(handlerName) + 1);
		} else {
			handlerRequestCount.put(handlerName, 1);
		}
	}



	@Override
	public void handle(HttpExchange t) throws IOException {
		StringBuilder response = new StringBuilder();
		for (String handler : handlerRequestCount.keySet()) {
			response.append(handler + " handler request count: " + handlerRequestCount.get(handler) + "\n");
		}
		
		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Content-Type",
				String.format("text/plain; charset=%s", StandardCharsets.UTF_8));
		
		t.sendResponseHeaders(200, 0);
		OutputStream os = t.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();
		
	}

}
