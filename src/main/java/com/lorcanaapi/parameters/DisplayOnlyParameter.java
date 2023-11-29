package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class DisplayOnlyParameter extends URLParameter {

	public DisplayOnlyParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		System.out.println("hjere");
		try {
		StringBuilder sb = new StringBuilder("SELECT ");
		for (String key: bit.getAllValues()) {
			sb.append(key + ", ");
		}
		response.getSqlQuery().setSelect(sb.toString().substring(0, sb.length() -2 ));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
