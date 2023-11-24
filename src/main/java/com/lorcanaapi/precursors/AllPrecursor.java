package com.lorcanaapi.precursors;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;

public class AllPrecursor extends URLPrecursor{


	public AllPrecursor(String precursorString) {
		super(precursorString);
	}

	@Override
	public String getUpdatedURLForHandler(String handler, String URL, APIResponse response) {
		switch (handler) {
			
		case "cards":
			return modifyCardsResponse(URL);
		case "sets":
			return modiftSetsResponse(URL);
		default:
			System.out.println("rawr");
			response.setErrored(true);
			response.setErrorMessage("invalid_handler", getPrecursorString() + " is not appliciable for handler '/" + handler + "/'!", 200);
			return URL;
		}
		
	}
	
	private String modifyCardsResponse(String URL) {
		StringBuilder newValue = new StringBuilder(URL);
		// If it has no parameters yet, add a ?, if some parameters are already present, add an &
		// If this was not the ALL precursor, the code below would be uncommented. 
			//		if (URL.endsWith(getPrecursorString())) {
			//			newValue.append("?");
			//		} else {
			//			newValue.append("&");
			//		}
			//		System.out.println("URL pre is:" + URL);
		return newValue.toString();
	}
	private String modiftSetsResponse(String URL) {
		return URL;
		
	}

}
