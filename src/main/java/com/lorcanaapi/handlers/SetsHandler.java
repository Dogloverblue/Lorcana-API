package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;
import com.lorcanaapi.parameters.DisplayOnlyParameter;
import com.lorcanaapi.parameters.PageParameter;
import com.lorcanaapi.parameters.PageSizeParameter;
import com.lorcanaapi.parameters.SearchParameter;
import com.lorcanaapi.parameters.StrictParameter;
import com.lorcanaapi.precursors.AllPrecursor;
import com.lorcanaapi.precursors.FetchPrecursor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SetsHandler implements HttpHandler {

	ParameterManager pm = null;
	PrecursorManager pem = null;

	@Override
	public void handle(HttpExchange t) throws IOException {
		if (pm == null || pem == null) {
			pm = defaultParameterValues();
			pem = defaultPrecursorValues();
		}
		APIResponse response = new APIResponse("set_info");
		HashMap<String, String> paramMap = new HashMap<>();
		String url = t.getRequestURI().toString();
		ArrayList<String> precursors = new ArrayList<>(pem.getAllPrecursors());

		boolean foundPrecursor = false;
		String precursorString = "";
		String precursor = "";
		for (String pre : precursors) {
//    		System.out.println("pre:" + pre);
			precursorString += "/sets/" + pre + ", ";
			if (url.contains("/sets/" + pre)) {
//    			System.out.println("Founbd:" + pre);
				foundPrecursor = true;
				precursor = pre;
				System.out.println("URL BEFORE:" + url);
				url = pem.getPrecursorFromString(pre).getUpdatedURLForHandler("sets", url, response);
//    			System.out.println("URL AFTER:" + url);
				url = url.replace("/sets/" + pre + "?", "");
				url = url.replace("/sets/" + pre + "", "");
			}
		}
		if (foundPrecursor == false) {
//    		System.out.println("Founbdnt");
			response.setErrored(true);
			response.setErrorMessage("invalid_url", "Invalid URL! The current URLs avaliable for /sets/ is "
					+ precursorString.substring(0, precursorString.length() - 2), 404);
		} else {

			for (String param : url.split("\\&")) {
//    		System.out.println(param);
				paramMap.put(param.split("\\=")[0].toLowerCase(), param);
			}
			for (URLParameter up : pm.getSortedParameters()) {
				if (paramMap.containsKey(up.getParameterKey()) || up.getParameterKey().equals("sqlexecutor")) {
//			System.out.println("found " + up.getParameterKey());
					if (!up.isPrecursorValid(precursor)) {
//				System.out.println("precursor NOT valid");
						response.setErrored(true);
						response.setErrorMessage("invalid_parameter",
								"You cant use '" + up.getParameterKey() + "' with endpoint /" + precursor + "!", 404);
						break;
					}
//			System.out.println("precursor valid");
					URIBit bit = new URIBit(paramMap.get(up.getParameterKey()));
//			System.out.println("here");
					up.modifyReponse(bit, response);
//			System.out.println("here2");
				} else {
//			System.out.println("did not find " + up.getParameterKey());
				}
			}
			t.getResponseHeaders().set("Content-Type",
					String.format("application/json; charset=%s", StandardCharsets.UTF_8));
			t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			t.sendResponseHeaders(200, 0);
			OutputStream os = t.getResponseBody();
			os.write(response.getResponse().getBytes());
			os.close();
		}
	}

	public ParameterManager defaultParameterValues() {
		ParameterManager pm = new ParameterManager(new SearchParameter("search", -1, "fetch"),
				new PageSizeParameter("pagesize", -2), new PageParameter("page", -1),
				new DisplayOnlyParameter("displayonly", -1), new StrictParameter("strict", -1, "fetch"));
		return pm;
	}

	public PrecursorManager defaultPrecursorValues() {
		PrecursorManager pm = new PrecursorManager(new AllPrecursor("all"), new FetchPrecursor("fetch"));
		return pm;
	}

}
