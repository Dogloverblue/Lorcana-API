package com.lorcanaapi.parameters;

import org.json.JSONArray;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class PageParameter extends URLParameter {

	public PageParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		try {
			int pageSize = response.getPageSize();
			int page = Integer.valueOf(bit.getValue());
			int startingPos = pageSize * (page - 1);
			System.out.println("the from is:" + response.getSqlQuery().getOrderBy() + " LIMIT " + startingPos + ", " + pageSize);
			response.getSqlQuery().setOrderBy(response.getSqlQuery().getOrderBy() + " LIMIT " + startingPos + ", " + pageSize);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("fail");
			response.setErrored(true);
			response.setErrorMessage("invalid_datatype", "'" + bit.getValue() + "' is not an integer! [Parameter=page]", 200);
		}
	}

}
