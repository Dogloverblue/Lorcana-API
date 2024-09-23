package com.lorcanaapi.parameters;

import java.util.ArrayList;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class FuzzyParameter extends URLParameter {

	public FuzzyParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	public FuzzyParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
//		String compareString = bit.getValue().split(",")[0];
		String name = bit.getValue();
		System.out.println(BulkParameterThread.getCardNames().size());
		try {
			ExtractedResult res = FuzzySearch.extractOne(name, BulkParameterThread.getCardNames());
			System.out.println(res.getString());
			response.getSqlQuery().setWhere("Where name = \"" + res.getString() + "\"");
			response.setSingleResponse(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		response.getSqlQuery().setOrderBy("ORDER BY LEVENSHTEIN_RATIO(Name, \"" + compareString +  "\") DESC");

	}

}
