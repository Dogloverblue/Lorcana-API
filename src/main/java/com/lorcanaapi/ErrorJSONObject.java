package com.lorcanaapi;

import org.json.JSONObject;

public class ErrorJSONObject extends JSONObject{
	
	public ErrorJSONObject(String code, int status, String details) {
		this.put("object", "error");
		this.put("code", code);
		this.put("status", status);
		this.put("details", details);
	}

}
