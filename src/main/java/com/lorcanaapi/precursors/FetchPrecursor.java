package com.lorcanaapi.precursors;

import com.lorcanaapi.APIResponse;

public class FetchPrecursor extends URLPrecursor {

	public FetchPrecursor(String precursorString) {
		super(precursorString);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUpdatedURLForHandler(String handler, String URL, APIResponse response) {
		switch (handler) {
		
		case "cards":
			return modifyCardsResponse(URL);
		default:
			response.setErrored(true);
			response.setErrorMessage("invalid_hander", getPrecursorString() + " is not appliciable for handler '/" + handler + "/'!", 200);
			return URL;
		}
	}
	
	private String modifyCardsResponse(String URL) {
		return URL;
	}

}
