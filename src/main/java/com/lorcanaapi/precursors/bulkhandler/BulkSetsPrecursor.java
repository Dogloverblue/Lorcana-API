package com.lorcanaapi.precursors.bulkhandler;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.precursors.URLPrecursor;

public class BulkSetsPrecursor extends URLPrecursor {

	public BulkSetsPrecursor() {
		super("sets");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUpdatedURLForHandler(String handler, String URL, APIResponse response) {
		switch (handler) {
	case "bulk":
		return modifyBulkResponse(URL);
	default:
		response.setErrored(true);
		response.setErrorMessage("invalid_hander", getPrecursorString() + " must only be used by '/bulk/' handler!", 200);
		return URL;
		}
	}
	
	private String modifyBulkResponse(String URL) {
		StringBuilder newValue = new StringBuilder(URL);
				if (URL.endsWith(getPrecursorString())) {
					newValue.append("?");
				} else {
					newValue.append("&");
				}
				newValue.append("bulktype=sets");
		return newValue.toString();
	}

}
