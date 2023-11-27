package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;
import com.lorcanaapi.parameters.DisplayOnlyParameter;
import com.lorcanaapi.parameters.FuzzyMinimunParameter;
import com.lorcanaapi.parameters.FuzzyParameter;
import com.lorcanaapi.parameters.FuzzyTestParemeter;
import com.lorcanaapi.parameters.PageParameter;
import com.lorcanaapi.parameters.PageSizeParameter;
import com.lorcanaapi.parameters.SearchParameter;
import com.lorcanaapi.parameters.StrictParameter;
import com.lorcanaapi.precursors.AllPrecursor;
import com.lorcanaapi.precursors.FetchPrecursor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


	public class CardsHandler implements HttpHandler {
		static int TextRequestCount = 0;
	    ParameterManager pm = null;
	    PrecursorManager pem = null;
        @Override
        public void handle(HttpExchange t) throws IOException {
        	if (pm == null || pem == null) {
        		pm = defaultParameterValues();
        		pem = defaultPrecursorValues();
        	}

        	System.out.println("URL is " + t.getRequestURI().toString());

        	APIResponse response = new APIResponse("card_info");
        	HashMap<String, String> paramMap = new HashMap<>();
        	String url =t.getRequestURI().toString();
        	String[] precursors = {"fetch", "all"};
        	
        	
        	boolean foundPrecursor = false;
        	String precursorString = "";
        	String precursor = "";
        	for (String pre: precursors) {
        		precursorString += "/cards/" + pre + ", ";
        		if (url.contains("/cards/" + pre) ) {
        			System.out.println("Founbd:" + pre);
        			foundPrecursor = true;
        			precursor = pre;
        			System.out.println("URL BEFORE:" + url);
        			url = pem.getPrecursorFromString(pre).getUpdatedURLForHandler("cards", url, response);
        			System.out.println("URL AFTER:" + url);
        			url = url.replace("/cards/" + pre + "?", "");
        			url = url.replace("/cards/" + pre + "", "");
        		}
        	}
        	if (foundPrecursor == false) {
        		System.out.println("Founbdnt");
        		response.setErrored(true);
        		response.setErrorMessage("invalid_url", "Invalid URL! The current URLs avaliable for /cards/ is " + precursorString.substring(0, precursorString.length() - 2), 404);
        	} else {
        		
        	for (String param: url.split("\\&")) {
        		System.out.println(param);
        		paramMap.put(param.split("\\=")[0].toLowerCase(), param);
        	}
		for (URLParameter up: pm.getSortedParameters()) {
			if (paramMap.containsKey(up.getParameterKey()) || up.getParameterKey().equals("sqlexecutor")) {
				System.out.println("found " + up.getParameterKey());
				if (!up.isPrecursorValid(precursor)) {
					System.out.println("precursor NOT valid");
					response.setErrored(true);
					response.setErrorMessage("invalid_parameter", "You cant use '" + up.getParameterKey() + "' with endpoint /" + precursor + "!", 404);
					break;
				}
				System.out.println("precursor valid");
				URIBit bit = new URIBit(paramMap.get(up.getParameterKey()));
				System.out.println("here");
				up.modifyReponse(bit, response);
				System.out.println("here2");
			} else {
				System.out.println("did not find " + up.getParameterKey());
			}
		}
        	}
		System.out.println("SQL Query is " + response.getSqlQuery().getQuery());                                                                                          
//		System.out.println("Bit is " + bit);                                                                                                                              
//		System.out.println("REPONSE IS " + response.getResponse());     
		TextRequestCount++;                                                                                                                                               
			t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));                                            
			t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");                                                                                                                                                      
            t.sendResponseHeaders(200, 0);                                                                                                  
            OutputStream os = t.getResponseBody();                                                                                                                        
            os.write(response.getResponse().getBytes());                                                                                                                  
            os.close();                                                                                                                                                   
                                                                                                                                                                          
        }                                                                                                                                                                 
	    public CardsHandler() {
	    }
	    
	    public ParameterManager defaultParameterValues() {
	    	ParameterManager pm = new ParameterManager(
	    			new SearchParameter("search", -1, "fetch"),
	    			new PageSizeParameter("pagesize", -2),
	    			new PageParameter("page", -1),
	    			new DisplayOnlyParameter("displayonly", -1),
	    			new StrictParameter("strict", -1, "fetch"),
	    			new FuzzyParameter("fuzzy", -1, "fetch"),
	    			new FuzzyMinimunParameter("fuzzymin", -1, "fetch"),
	    			new FuzzyTestParemeter("fuzzytest", -1)
	    			);
	    	return pm;
	    }
	    public PrecursorManager defaultPrecursorValues() {
	    	PrecursorManager pm = new PrecursorManager(
	    			new AllPrecursor("all"),
	    			new FetchPrecursor("fetch")
	    			);
	    	return pm;
	    }
	   

}
