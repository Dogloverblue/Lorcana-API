package com.lorcanaapi.precursors;

import com.lorcanaapi.APIResponse;

abstract public class URLPrecursor {

	
	abstract public String getUpdatedURLForHandler(String handler, String url, APIResponse response); 
	
	String precursorString;
	
	public String getPrecursorString() {
		return precursorString;
	}
	public URLPrecursor(String precursorString) {
		this.precursorString = precursorString;
	}

}
