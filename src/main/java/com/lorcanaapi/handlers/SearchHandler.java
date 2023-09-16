package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SearchHandler implements HttpHandler {

	private HashMap<String, String> equalsParams;
	private HashMap<String, String> containsParams;
	private HashMap<String, String> data;
	static int SearchRequestCount = 0;
	/**
	 * A HTTPHandler that can return only cards with certain attributes equal to a certain value
	 * @param data A hashmap of name-to-JSON data
	 */
	public SearchHandler(HashMap<String, String> data) {
		equalsParams = new HashMap<>();
		containsParams = new HashMap<>();
		this.data = data;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("Search response made; it was search request number " + SearchRequestCount);
		exchange.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        
		this.queryToMap(exchange.getRequestURI().toString());
		JSONArray passedCards = new JSONArray();
		
		for (String key: data.keySet()) {
			try {
			boolean passed = true;
			JSONObject jsonO = new JSONObject(data.get(key));
			equalsLoop:
			for (String key2: equalsParams.keySet()) {
				JSONArray ja;
				if ((ja = jsonO.optJSONArray(key2)) != null) {
					passed = false;
					for (Object d: ja.toList()) {
						if (String.valueOf(d).equalsIgnoreCase(equalsParams.get(key2))) {
							passed = true;
							continue equalsLoop;
						} else {
							//System.out.println(String.valueOf(d) + " : " + equalsParams.get(key2));
						}
					}
				}
				if (!(String.valueOf(jsonO.get(key2)).replace(" ", "_").equalsIgnoreCase(String.valueOf(equalsParams.get(key2))))) {
					passed = false;
				} else {
				}
				
			}
			containsLoop:
			for (String key3: containsParams.keySet()) {
				JSONArray ja;
				if ((ja = jsonO.optJSONArray(key3)) != null) {
					passed = false;
					for (Object d: ja.toList()) {
						if (String.valueOf(d).toLowerCase().contains(containsParams.get(key3).toLowerCase())) {
							passed = true;
							continue containsLoop;
						}
					}
				}
				if (!(String.valueOf(jsonO.get(key3)).replace(" ", "_").toLowerCase().contains(String.valueOf(containsParams.get(key3)).toLowerCase()))) {
					passed = false;
				}
			}
			if (passed == true) {
				if (exchange.getRequestURI().toString().toLowerCase().contains("displaydetails=true")) {
					System.out.println("readlll");
					passedCards.put(jsonO);
				} else {
					System.out.println(exchange.getRequestURI().toString());
					passedCards.put(key);
				}
			}
			} catch (JSONException e) {
			}
			
		}
		
		 OutputStream os = exchange.getResponseBody();
		 exchange.sendResponseHeaders(200, passedCards.toString().length());
         os.write(passedCards.toString().getBytes());
         os.close();
		
	}
	/**
	 * Puts all the key-value pairs in the URL into their appropriate HashMaps
	 * @param query The URL of the request
	 */
	public void queryToMap(String query){

	    equalsParams.clear();
	    containsParams.clear();
	    query = query.substring(query.indexOf("?") + 1, query.length());
	    for (String param : query.split("&")) {
	    	if (param.toLowerCase().contains("displaydetails")) {
	    		System.out.println(":read");
	    		continue;
	    	}
	    	if(param.contains("~")) {
	    		String pair[] = param.split("~");

		        if (pair.length>1) {
		            containsParams.put(pair[0], pair[1]);

		        }else{
		            containsParams.put(pair[0], "");
		        }
		        continue;
	    	}
	        if (param.contains("=")) {
	        	String pair[] = param.split("=");
		        if (pair.length>1) {

		            equalsParams.put(pair[0], pair[1]);

		        }else{

		            equalsParams.put(pair[0], "");

		        }
	        }

	    }

}
}
