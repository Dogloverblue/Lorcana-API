package com.lorcanaapi.handlers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lorcanaapi.ErrorJSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StrictHandler implements HttpHandler {
	File cardDirectory = new File("src//data//cards");
	static int StrictRequestCount = 0;
	String response;
    @Override
    public void handle(HttpExchange t) throws IOException {
	System.out.println("Strict response made; it was Strict request number " + StrictRequestCount );
	StrictRequestCount++;
	String cardName = t.getRequestURI().toString().replace("/strict/", "");
	JSONArray jsonToReturn = new JSONArray();
	String[] args = cardName.split(";");
	if (args.length < 2) {
		File cardFile = new File(cardDirectory + "//" + args[0] + ".txt");
//		if ()
		response = new JSONObject(new String(Files.readAllBytes(Paths.get(cardFile.getAbsolutePath())))).toString();
	} else if (args.length > 1000) {
		response = new ErrorJSONObject("request_too_large", 413, "That request is too large! You can only request up to 1000 requests per request").toString();
	}
	else {
	for (String card: cardName.split(";")) {
		File cardFile = new File(cardDirectory + "//" + card + ".txt");
		if (cardFile.exists()) {
			jsonToReturn.put(new JSONObject(new String(Files.readAllBytes(Paths.get(cardFile.getAbsolutePath())))));
		}
	}
	
	response = jsonToReturn.toString();
	}
    t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        t.sendResponseHeaders(200, response.length());
  
        OutputStream os = t.getResponseBody();
        
        os.write(response.getBytes());
        os.close();
    }
    public StrictHandler() {
    	
    }
}