package com.lorcanaapi.parameters;

import java.util.HashMap;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class StrictParameter extends URLParameter {

public StrictParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
	}

	public StrictParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		StringBuilder where = new StringBuilder("WHERE ");
		bit.decodeURL();

		for (String v: bit.getAllValues()) {
//			System.out.println("v si " +v);
			where.append("(NAME ='" + v.toLowerCase() + "')");
			where.append(" OR ");
		}
		response.getSqlQuery().setWhere(where.toString().substring(0, where.length() - 4));

	}

}
