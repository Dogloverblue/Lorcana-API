package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class SortDirectionParameter extends URLParameter{

	public SortDirectionParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		String value = bit.getValue().toLowerCase();
		
		String modifier = value.startsWith("desc") ? "DESC" : "ASC";
		
		response.getSqlQuery().setOrderBy(response.getSqlQuery().getOrderBy() + " " + modifier);
		
	}
	
	
	

}
