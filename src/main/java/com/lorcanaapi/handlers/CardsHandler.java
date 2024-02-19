package com.lorcanaapi.handlers;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.DisplayOnlyParameter;
import com.lorcanaapi.parameters.FuzzyMinimunParameter;
import com.lorcanaapi.parameters.FuzzyParameter;
import com.lorcanaapi.parameters.FuzzyTestParemeter;
import com.lorcanaapi.parameters.PageParameter;
import com.lorcanaapi.parameters.PageSizeParameter;
import com.lorcanaapi.parameters.SearchParameter;
import com.lorcanaapi.parameters.StrictParameter;
import com.lorcanaapi.precursors.AllPrecursor;
import com.lorcanaapi.precursors.FetchPrecursor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpHandler;

public class CardsHandler extends URLHandler implements HttpHandler {
	static int TextRequestCount = 0;

	public CardsHandler() {
		super("cards", "card_info", defaultParameterValues(), defaultPrecursorValues());
	}

	private static ParameterManager defaultParameterValues() {
		ParameterManager pm = new ParameterManager(
				new SearchParameter("search", -1, "fetch"),
				new PageSizeParameter("pagesize", -2), 
				new PageParameter("page", -1),
				new DisplayOnlyParameter("displayonly", -1), 
				new StrictParameter("strict", -1, "fetch"),
				new FuzzyParameter("fuzzy", -1, "fetch"), 
				new FuzzyMinimunParameter("fuzzymin", -1, "fetch"),
				new FuzzyTestParemeter("fuzzytest", -1));
		return pm;
	}

	private static PrecursorManager defaultPrecursorValues() {
		PrecursorManager pm = new PrecursorManager(
				new AllPrecursor("all"), 
				new FetchPrecursor("fetch"));
		return pm;
	}

}
