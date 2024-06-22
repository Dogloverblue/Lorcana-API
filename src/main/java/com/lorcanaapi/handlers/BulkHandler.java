package com.lorcanaapi.handlers;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.BulkTypeParameter;
import com.lorcanaapi.precursors.PrecursorManager;
import com.lorcanaapi.precursors.bulkhandler.BulkCardsPrecursor;
import com.lorcanaapi.precursors.bulkhandler.BulkSetsPrecursor;
import com.sun.net.httpserver.HttpHandler;

public class BulkHandler extends URLHandler implements HttpHandler{

	public BulkHandler() {
		super("bulk", "DONOTFETCH", defaultParameterValues(), defaultPrecursorValues());

	}

	private static ParameterManager defaultParameterValues() {
		ParameterManager pm = new ParameterManager(new BulkTypeParameter());
		return pm;
	}

	private static PrecursorManager defaultPrecursorValues() {
		PrecursorManager pm = new PrecursorManager(new BulkCardsPrecursor(), new BulkSetsPrecursor());
		return pm;
	}

}
