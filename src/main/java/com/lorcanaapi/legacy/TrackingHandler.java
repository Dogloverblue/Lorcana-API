package com.lorcanaapi.legacy;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TrackingHandler implements HttpHandler {
			static int TextRequestCount = 0;
			static int JSONRequestCount = 0;
			static int FuzzyRequestCount = 0;
			static int StrictRequestCount = 0;
			static int SearchRequestCount = 0;
		    String response;
	        @Override
	        public void handle(HttpExchange t) throws IOException {
	        	String uri = t.getRequestURI().toString();
	        	StringBuilder sb = new StringBuilder();
	        	sb.append("Text request count: " + TextRequestCount + "\n");
	        	sb.append("JSON request count: " + JSONRequestCount + "\n");
	        	sb.append("Fuzzy request count: " + FuzzyRequestCount + "\n");
	        	sb.append("Strict request count: " + StrictRequestCount + "\n");
	        	sb.append("Search request count: " + SearchRequestCount);
	        	response = sb.toString();
	            t.sendResponseHeaders(200, response.length());
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
	        }
		    public TrackingHandler() {
			    
		    }
			public static int getTextRequestCount() {
				return TextRequestCount;
			}
			public static void setTextRequestCount(int textRequestCount) {
				TextRequestCount = textRequestCount;
			}
			public static int getJSONRequestCount() {
				return JSONRequestCount;
			}
			public static void setJSONRequestCount(int jSONRequestCount) {
				JSONRequestCount = jSONRequestCount;
			}
			public static int getFuzzyRequestCount() {
				return FuzzyRequestCount;
			}
			public static void setFuzzyRequestCount(int fuzzyRequestCount) {
				FuzzyRequestCount = fuzzyRequestCount;
			}
			public static int getStrictRequestCount() {
				return StrictRequestCount;
			}
			public static void setStrictRequestCount(int strictRequestCount) {
				StrictRequestCount = strictRequestCount;
			}
			public static int getSearchRequestCount() {
				return SearchRequestCount;
			}
			public static void setSearchRequestCount(int searchRequestCount) {
				SearchRequestCount = searchRequestCount;
			}
		    
	}


