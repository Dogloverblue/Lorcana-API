package com.lorcanaapi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class URIBit{
	
	String bit;

	public URIBit(String URIBit) {
		bit = URIBit;
	}
	
	public String getKey() {
		return bit.split("\\=")[0];
	}
	public String getValue() {
		return bit.split("\\=")[1];
	}
	
//	public List<String> getAllKeys() {
//		List<String> keys = new ArrayList<String>();
//		boolean first = true;
//		for (String str: bit.split("\\;")) {
////			if (first) {
////				first = false;
////				continue;
////			}
//			keys.add(str.split("\\=")[0]);
//		}
//		return keys;
//	}
	
	public List<String> getAllValues() {
		List<String> keys = new ArrayList<String>();
		String bitNoKey = bit.substring(bit.indexOf("=") + 1, bit.length());
		for (String str: bitNoKey.split("\\;")) {
			keys.add(str);
		}
		return keys;
	}
	
	public void decodeURL() {
		try {
			bit = java.net.URLDecoder.decode(bit, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getKeyValuePairsWithSeperator(String regex) {
		HashMap<String, String> pairs = new HashMap<String, String>();
		for (String str: bit.split(regex)) {
			pairs.put(str.split("\\=")[0], str.split("\\=")[1]);
		}
		return pairs;
	}
//	public HashMap<String, String> getKeyValuePairs() {
//		HashMap<String, String> pairs = new HashMap<String, String>();
//		for (String str: bit.split("\\;")) {
//			pairs.put(str.split("\\=")[0], str.split("\\=")[1]);
//		}
//		return pairs;
//	}

	@Override
	public String toString() {
		return bit;
	}

	
	
	


}
