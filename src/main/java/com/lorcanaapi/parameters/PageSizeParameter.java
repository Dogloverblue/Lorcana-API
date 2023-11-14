package com.lorcanaapi.parameters;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class PageSizeParameter extends URLParameter {

	public PageSizeParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		try {
		response.setPageSize(Integer.valueOf(bit.getValue()));
		} catch (NumberFormatException e) {
			response.setErrored(true);
			response.setErrorMessage("invalid_datatype", "\"" + bit.getValue() + "\" is not an integer! [Parameter=pagesize]", 200);
		}

	}

}
