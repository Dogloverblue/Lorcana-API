package com.lorcanaapi.handlers;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.DisplayOnlyParameter;
import com.lorcanaapi.parameters.PageParameter;
import com.lorcanaapi.parameters.PageSizeParameter;
import com.lorcanaapi.parameters.SearchParameter;
import com.lorcanaapi.parameters.StrictParameter;
import com.lorcanaapi.precursors.AllPrecursor;
import com.lorcanaapi.precursors.FetchPrecursor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpHandler;

public class SetsHandler extends URLHandler implements HttpHandler {

		static int TextRequestCount = 0;

		public SetsHandler() {
			super("sets", "set_info", defaultParameterValues(), defaultPrecursorValues());
		}

	private static ParameterManager defaultParameterValues() {
		ParameterManager pm = new ParameterManager(new SearchParameter("search", -1, "fetch"),
				new PageSizeParameter("pagesize", -2), new PageParameter("page", -1),
				new DisplayOnlyParameter("displayonly", -1), new StrictParameter("strict", -1, "fetch"));
		return pm;
	}

	private static PrecursorManager defaultPrecursorValues() {
		PrecursorManager pm = new PrecursorManager(new AllPrecursor("all"), new FetchPrecursor("fetch"));
		return pm;
	}

}
