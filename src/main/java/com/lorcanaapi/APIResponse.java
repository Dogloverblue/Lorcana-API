package com.lorcanaapi;

import org.json.JSONArray;

public class APIResponse {
	
	protected String response;
	protected int pageSize = 50;
	private boolean isErrored = false;
	private ErrorMessageResponse errorMessage = new ErrorMessageResponse("should_never_see_this_error", "If you see this message, you have encountered a bug... Please contact the developer", -1);
	private SQLQuery sqlQuery = new SQLQuery();
	private String reference;
	private boolean useSQL;
	
	public String getResponse() {
		if (isErrored()) {
			return getErrorMessage().getFullErrorAsJSON();
		} else {
		return response;
		}
	}
	public JSONArray getResponseAsJSONArray() {
		try {
		return new JSONArray(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void setResponse(String newResponse) {
		response = newResponse;
	}
	

	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int size) {
		pageSize = size;
	}
	

	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public boolean isErrored() {
		return isErrored;
	}

	public void setErrored(boolean isErrored) {
		this.isErrored = isErrored;
	}

	public ErrorMessageResponse getErrorMessage() {
		return errorMessage;
	}
	public String getErrorBlockAsJSON() {
		return "{\"code\":\"request_too_large\",\"details\":\"That request is too large! You can only request up to 1000 requests per request\",\"object\":\"error\",\"status\":413}";
	}

	public void setErrorMessage(ErrorMessageResponse errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setErrorMessage(String code, String description, int status) {
		this.errorMessage = new ErrorMessageResponse(code, description, status);
	}
	
	public SQLQuery getSqlQuery() {
		return sqlQuery;
	}
	public void setSqlQuery(SQLQuery sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	@Override
	public String toString() {
		return this.getResponse();
	}

	
	/**
	 * Constucts a new APIResponse class, the class used for handling everything about a API response, including
	 * SQL queries and JSONArrays
	 * @param tableName The name of the SQL table to make queries on. If set to 'DONOTFETCH', queries will not be executed
	 */

	public APIResponse(String tableName) {
		this.getSqlQuery().setFrom("FROM " + tableName);
		
	}
	
	public APIResponse() {
		
	}

}
