package com.lorcanaapi.parameters;

import java.util.HashMap;

import com.lorcanaapi.APIResponse;
import com.lorcanaapi.URIBit;
import com.lorcanaapi.URLParameter;

public class SearchParameter extends URLParameter {

	public SearchParameter(String parameterKey, int executionPriority, String... validPrecursors) {
		super(parameterKey, executionPriority, validPrecursors);
	}

	public SearchParameter(String parameterKey, int executionPriority) {
		super(parameterKey, executionPriority);
	}

	String[] seperators = { "=", "!=", "<", ">", "<=", ">=" };

	@Override
	public void modifyReponse(URIBit bit, APIResponse response) {
		try {
			StringBuilder where = new StringBuilder("WHERE ");
			String uri = bit.toString().toLowerCase().replace("search=", "");
			uri = uri.replace("%3c", "<");
			uri = uri.replace("%3e", ">");
			uri = uri.replace("%7c", "|");
			uri = uri.replace("%28", "(");
			uri = uri.replace("%29", ")");
			System.out.println("Bit is:" + bit);
			System.out.println("URI is:" + uri);
			int i = 0;
			for (String thing : uri.split(";")) {
				i++;
				String logicString = "AND";

				if (thing.startsWith("|")) {
					thing = thing.replaceFirst("\\|", "");
					logicString = "OR";
				}
				String appendWhat = "";
				if (thing.startsWith(")")) {
					thing = thing.replaceFirst("\\)", "");
					appendWhat = ") ";
				}

				for (String sep : seperators) {
					if (thing.contains(sep)) {
						// Making sure that >= are not double counted
						if (sep.equals("<") || sep.equals(">")) {
							if (thing.charAt(thing.indexOf(sep) + 1) == '=') {
								continue;
							}
						}
						if (sep.equals("=")) {
							if (thing.charAt(thing.indexOf(sep) - 1) == '>'
									|| thing.charAt(thing.indexOf(sep) - 1) == '<'
									|| thing.charAt(thing.indexOf(sep) - 1) == '!') {
								continue;
							}
						}
						String[] splt = thing.split(sep);
						// may want to consider not quote numbers
						if (i > 1) {
							where.append(logicString + " ");
						}
						where.append(splt[0] + " " + sep + " '" + splt[1] + "' ");
						
					}
				}
				where.append(appendWhat);
				appendWhat = "";
			}

			System.out.println("The where is:" + where.toString().substring(0, where.length()));
			response.getSqlQuery().setWhere(where.toString().substring(0, where.length()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
