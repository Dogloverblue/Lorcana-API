package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class FuzzyHandler implements HttpHandler {
		static int FuzzyRequestCount = 0;
	    String response = "sd";
	    static HashMap<String, String> data = new HashMap<String, String>();
      @Override
      public void handle(HttpExchange t) throws IOException {
		 System.out.println("Fuzzy response made; it was fuzzy request number " + FuzzyRequestCount);
		 FuzzyRequestCount++;
		 TrackingHandler.FuzzyRequestCount++;
		 System.out.println("Oabam,, hamburger susys");
     	 t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
     	t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
     	 ExtractedResult result = FuzzySearch.extractOne(String.valueOf(t.getRequestURI()).replace("/fuzzy/", ""), data.keySet());
          response = data.get(result.getString());
          t.sendResponseHeaders(200, response.length());
          OutputStream os = t.getResponseBody();
          os.write(response.getBytes());
          os.close();
      }
      public FuzzyHandler(HashMap<String, String> cardData) {
    	  for (String key: cardData.keySet()) {
    		 String value = cardData.get(key);
    		 try {
    		 JSONObject jo = new JSONObject(value);
    		 try {
    			 jo.getString("name");
    			 data.put(key, value);
    		 } catch (JSONException e) {
				System.out.println("Not adding " + key + " to /fuzzy/ because it's a non exact response");
			}
    		 } catch (JSONException e) {
    			 System.out.println("Problem with " + key);
    		 }
    		
    	  }
      }
     
	    
  }
