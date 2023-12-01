package com.lorcanaapi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.lorcanaapi.parameters.MandatorySQLExecutor;

public class ParameterManager {
	
	ArrayList<URLParameter> parameters;
	
	public List<URLParameter> getParametersWithPriority(int priority) {
		List<URLParameter> paramList = new ArrayList<>();
		for (URLParameter p: parameters) {
			if (p.getExecutionPriority() == priority) {
				paramList.add(p);
			}
		}
		return paramList;
	}
	
	public ArrayList<URLParameter> getSortedParameters() {
		return parameters;
	}

	public ParameterManager(Collection<URLParameter> params) {
		registerParameters(params);
	}

	public ParameterManager(URLParameter... params) {
		registerParameters(Arrays.asList(params));
	}

	public ParameterManager() {

	}

	public void registerParameters(Collection<URLParameter> params) {
		parameters = new ArrayList<>(params);
		parameters.add(new MandatorySQLExecutor());
		Collections.sort(parameters);
	}

	public void registerParameters(URLParameter... params) {
		registerParameters(Arrays.asList(params));
	}

	

}
