package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class RefParameter extends URLParameter {

	public RefParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
		// TODO Auto-generated constructor stub
	}

	public RefParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		response.setReference(bit.getValue());
		// TODO Auto-generated method stub

	}

}
