package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.SQLQuery;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class FuzzyMinimunParameter extends URLParameter{


	public FuzzyMinimunParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}
	public FuzzyMinimunParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		
		int i = bit.getValue().lastIndexOf(","); 
		String[] a =  {bit.getValue().substring(0, i), bit.getValue().substring(i+1)};
		String compareString = a[0];
		String minValue = a[1];
		
		if (response.getSqlQuery().getWhere().equals(SQLQuery.DEFAULT_WHERE_VALUE)) {
			response.getSqlQuery().setWhere("WHERE LEVENSHTEIN_RATIO(Name, \"" + compareString + "\") > " + minValue);
		}
		
		response.getSqlQuery().setOrderBy("ORDER BY LEVENSHTEIN_RATIO(Name, \"" + compareString +  "\") DESC");
		
	}

}
