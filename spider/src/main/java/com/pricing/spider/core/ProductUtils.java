package com.pricing.spider.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ProductUtils {

	private static final String REGEX = "[₹,?,]";
	private static final Map<Character, String> map = new HashMap<>();

	static {
		for (int i = 0; i < REGEX.length(); i++) {
			map.put(REGEX.charAt(i), "");
		}
	}

	public static Double getIntValue(final String val) {
		StringBuilder stringValue = new StringBuilder(val);
		double value = 0.0;
		Matcher matcher = Pattern.compile(REGEX).matcher(val);
		int i = 0;
		try {
			while (matcher.find()) {
				stringValue.replace(matcher.start() - i, (matcher.start() + 1) - i,
						map.get(val.charAt(matcher.start())));
				i++;
			}
			value = Double.parseDouble(stringValue.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

}