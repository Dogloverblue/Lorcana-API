package com.lorcanaapi.precursors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.lorcanaapi.URLParameter;
import com.lorcanaapi.parameters.MandatorySQLExecutor;

public class PrecursorManager {

	
	HashMap<String, URLPrecursor> precursors = new HashMap<String, URLPrecursor>();
	
	public PrecursorManager(Collection<URLPrecursor> params) {
		registerParameters(params);
	}

	public PrecursorManager(URLPrecursor... params) {
		registerParameters(Arrays.asList(params));
	}

	public PrecursorManager() {

	}

	public void registerParameters(Collection<URLPrecursor> params) {
		for (URLPrecursor precursor : params) {
			precursors.put(precursor.getPrecursorString(), precursor);
		}
	}

	public void registerParameters(URLPrecursor... params) {
		registerParameters(Arrays.asList(params));
	}
	
	public URLPrecursor getPrecursorFromString(String string) {
		return precursors.get(string);
	}

}
