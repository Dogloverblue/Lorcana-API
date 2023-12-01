package com.lorcanaapi.legacy;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StrictHandler implements HttpHandler {
	File cardDirectory = new File("src//data//cards");
	static int StrictRequestCount = 0;
	String response;
	ArrayList<String> names = null;

	@Override
	public void handle(HttpExchange t) throws IOException {
		if (names == null) {
			setupNames();
		}
		StrictRequestCount++;
		TrackingHandler.StrictRequestCount++;
		String cardName = t.getRequestURI().toString().replace("/strict/", "");
		JSONArray jsonToReturn = new JSONArray();
		String[] args = cardName.split(";");
		System.out.println(
				"Strict response made of size " + args.length + "; it was Strict request number " + StrictRequestCount);
		System.err.println("[err]Strict response made of size " + args.length + "; it was Strict request number "
				+ StrictRequestCount);
		if (args.length > 1000) {
			response = new ErrorJSONObject("request_too_large", 413,
					"That request is too large! You can only request up to 1000 requests per request").toString();
		} else {
			for (String card : cardName.split(";")) {
				if (!card.contains("-")) {
					JSONObject jo = new JSONObject();
					if (new File(cardDirectory + "//" + card + ".txt").exists()) {
						jsonToReturn.put(new JSONObject(
								Files.readString(Paths.get(cardDirectory + "//" + card + ".txt")).toString()));
						continue;
					}
					for (String name : names) {
						if (name.startsWith(card)) {
							try {
								JSONObject jo2 = new JSONObject(
										Files.readString(Paths.get(cardDirectory + "//" + name + ".txt")).toString());
								jo.put(jo2.getString("subtitle").toLowerCase().replace(" ", "_"), jo2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					jsonToReturn.put(jo);
				}
				File cardFile = new File(cardDirectory + "//" + card + ".txt");
				if (cardFile.exists()) {
					jsonToReturn
							.put(new JSONObject(new String(Files.readAllBytes(Paths.get(cardFile.getAbsolutePath())))));
				}
			}
			if (jsonToReturn.length() < 2) {
				response = jsonToReturn.getJSONObject(0).toString();
			} else {
				response = jsonToReturn.toString();
			}
		}
		t.getResponseHeaders().set("Content-Type",
				String.format("application/json; charset=%s", StandardCharsets.UTF_8));
		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.sendResponseHeaders(200, response.length());

		OutputStream os = t.getResponseBody();

		os.write(response.getBytes());
		os.close();
	}

	public StrictHandler() {

	}

	private void setupNames() {
		names = new ArrayList<String>();
		for (File file : cardDirectory.listFiles()) {
			names.add(LAPIUtils.removeFileExtension(file.getName(), false));
		}
	}
}