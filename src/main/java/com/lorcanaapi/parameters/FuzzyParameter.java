package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.SQLQuery;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class FuzzyParameter extends URLParameter{

	

	public FuzzyParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}
	public FuzzyParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		String compareString = bit.getValue().split(",")[0];
		
		
		response.getSqlQuery().setOrderBy("ORDER BY LEVENSHTEIN_RATIO(Name, \"" + compareString +  "\") DESC");
		
	}

}
