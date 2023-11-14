package com.lorcanaapi;

public class ErrorMessageResponse {
	
	String code;
	String error;
	int status;
	public ErrorMessageResponse(String code, String error, int status) {
		this.code = code;
		this.error = error;
		this.status = status;
	}
	
	public String getFullErrorAsJSON() {
		return "{\"code\":\"" + getCode() + 
				"\",\"details\":\"" + error + 
				"\",\"object\":\"error\"" + 
				",\"status\":" + status + "}";
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	

}
