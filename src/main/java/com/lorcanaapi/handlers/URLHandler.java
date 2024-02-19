package com.lorcanaapi.handlers;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class URLHandler implements HttpHandler{
	
	ParameterManager paramManager = null;
	PrecursorManager precursorManager = null;
	String handlerName;
	String sqlTable;
	
	public URLHandler(String handlerName, String sqlTable, ParameterManager paramManager, PrecursorManager precursorManager) {
		this.paramManager = paramManager;
		this.precursorManager = precursorManager;
		this.handlerName = handlerName;
		this.sqlTable = sqlTable;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		System.out.println("URL is " + t.getRequestURI().toString());

		APIResponse response = new APIResponse(sqlTable);
		HashMap<String, String> paramMap = new HashMap<>();
		String url = t.getRequestURI().toString().replace("%27", "%27%27");
		System.out.println("URL IS" + url);
		
		String[] precursors = { "fetch", "all" };

		boolean foundPrecursor = false;
		String precursorString = "";
		String precursor = "";
		for (String pre : precursors) {
			precursorString += "/" + handlerName + "/" + pre + ", ";
			if (url.contains("/" + handlerName + "/" + pre)) {
//				System.out.println("Founbd:" + pre);
				foundPrecursor = true;
				precursor = pre;
//				System.out.println("URL BEFORE:" + url);
				url = precursorManager.getPrecursorFromString(pre).getUpdatedURLForHandler(handlerName, url, response);
//				System.out.println("URL AFTER:" + url);
				url = url.replace("/" + handlerName + "/" + pre + "?", "");
				url = url.replace("/" + handlerName + "/" + pre + "", "");
			}
		}
		if (foundPrecursor == false) {
//			System.out.println("Founbdnt");
			response.setErrored(true);
			response.setErrorMessage("invalid_url", "Invalid URL! The current URLs avaliable for /cards/ is "
					+ precursorString.substring(0, precursorString.length() - 2), 404);
		} else {

			for (String param : url.split("\\&")) {
//				System.out.println(param);
				paramMap.put(param.split("\\=")[0].toLowerCase(), param);
			}
			for (URLParameter up : paramManager.getSortedParameters()) {
				if (paramMap.containsKey(up.getParameterKey()) || up.getParameterKey().equals("sqlexecutor")) {
//					System.out.println("found " + up.getParameterKey());
					if (!up.isPrecursorValid(precursor)) {
//						System.out.println("precursor NOT valid");
						response.setErrored(true);
						response.setErrorMessage("invalid_parameter",
								"You cant use '" + up.getParameterKey() + "' with endpoint /" + precursor + "!", 404);
						break;
					}
//					System.out.println("precursor valid");
					URIBit bit = new URIBit(paramMap.get(up.getParameterKey()));
//					System.out.println("here");
					up.modifyReponse(bit, response);
//					System.out.println("here2");
				} else {
//					System.out.println("did not find " + up.getParameterKey());
				}
			}
		}
//		if (response.isErrored()) {
			System.out.println("SQL Query is " + response.getSqlQuery().getQuery());
//		}
//		System.out.println("Bit is " + bit);                                                                                                                              
//		System.out.println("REPONSE IS " + response.getResponse());     
//		TextRequestCount++;
		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Content-Type",
				String.format("application/json; charset=%s", StandardCharsets.UTF_8));
		
		t.sendResponseHeaders(200, 0);
		OutputStream os = t.getResponseBody();
		os.write(response.getResponse().getBytes());
		os.close();

		
	}
	
	

}
