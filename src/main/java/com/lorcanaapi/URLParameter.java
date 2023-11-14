package com.lorcanaapi;

import java.util.Arrays;

abstract public class URLParameter implements Comparable<URLParameter>{

	abstract public void modifyReponse(URIBit URIBit, APIResponse response);

	/**
	 * Returns the priority of this parameter. The higher the priority, the earlier
	 * it gets executed. Values that are the same will be executed in some order
	 * /shrug
	 * 
	 * @return the execution priority
	 */
	public int getExecutionPriority() {
		return executionPriority;
	}

	public String getParameterKey() {
		return parameterKey;
	}
	private String[] precursors = null;
	private String parameterKey;
	private int executionPriority;

	public URLParameter(String parameterKey, int executionPriority) {
		this.parameterKey = parameterKey;
		this.executionPriority = executionPriority;
	}
	public URLParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		this.parameterKey = parameterKey;
		this.executionPriority = executionPriority;
		this.precursors = validPrecursors;
	}
	public boolean isPrecursorValid(String precursor) {
		if (precursors == null) {
			System.out.println("is null!");
			return true;
		} else {
			System.out.println("Not null, ");
			for (String pre: precursors) {
				System.out.println("1: " + pre);
			}
		}
		
		return Arrays.stream(precursors).anyMatch(precursor::equalsIgnoreCase);
	}
	
	@Override
	public int compareTo(URLParameter o) {
		return getExecutionPriority() > o.getExecutionPriority() ? 1 : getExecutionPriority() < o.getExecutionPriority() ? -1 : 0;
	}

}
