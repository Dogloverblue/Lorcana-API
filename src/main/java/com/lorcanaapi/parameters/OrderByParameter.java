package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class OrderByParameter extends URLParameter{

	public OrderByParameter(String key, int executionPriority) {
		super(key, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		response.getSqlQuery().setOrderBy("ORDER BY " + bit.getValue());
		
	}

}
