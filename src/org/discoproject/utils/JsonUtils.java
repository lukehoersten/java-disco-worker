package org.discoproject.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils for messing with json formatted data.
 * 
 * @author agraddy
 */
public class JsonUtils {

	/**
	 * To array.
	 * 
	 * @param jsonString the json string
	 * @return the string[]
	 */
	public static List<String> asArray(final String jsonString) {

		if (!jsonString.startsWith("[")) {
			throw new IllegalArgumentException(jsonString + " does not appear to be an array");
		}

		final List<String> elements = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();

		int depth = 0;

		for (int i = 1; i < jsonString.length() - 1; i++) {
			final char charAt = jsonString.charAt(i);

			if (openValue(charAt)) {
				depth++;
			}
			else if (closeValue(charAt)) {
				depth--;
			}

			if (depth == 0 && charAt == ',') { // end of element
				elements.add(sb.toString());
				sb.delete(0, sb.length());
			}
			else {
				sb.append(charAt);
			}
		}

		elements.add(sb.toString());

		return elements;
	}

	private static boolean openValue(final char charAt) {
		return charAt == '[' || charAt == '{';
	}

	private static boolean closeValue(final char charAt) {
		return charAt == ']' || charAt == '{';
	}

	/**
	 * As string.
	 * 
	 * @param index the index
	 * @param jsonArray the json array
	 * @return the string
	 */
	public static String asString(final int index, final List<String> jsonArray) {
		return jsonArray.get(index).replaceAll("\"", "");
	}

	/**
	 * As integer.
	 * 
	 * @param index the index
	 * @param jsonArray the json array
	 * @return the int
	 */
	public static int asInteger(final int index, final List<String> jsonArray) {
		return Integer.parseInt(jsonArray.get(index));
	}

	/**
	 * Build a map object out of the json string
	 * 
	 * @param jsonString the json string
	 * @return the map
	 */
	public static Map<String, String> asObject(final String jsonString) {
		if (!jsonString.startsWith("{")) {
			throw new IllegalArgumentException(jsonString + " does not appear to be an object");
		}

		final Map<String, String> keyToValue = new HashMap<String, String>();
		final StringBuilder sb = new StringBuilder();

		int depth = 0;
		boolean quote = false;
		String key = null;

		for (int i = 1; i < jsonString.length() - 1; i++) {
			final char charAt = jsonString.charAt(i);

			if (openValue(charAt)) {
				depth++;
			}
			else if (closeValue(charAt)) {
				depth--;
			}

			if (charAt == '"') {
				quote = !quote;
			}

			if (depth == 0 && charAt == ':' && !quote) { // end of key
				key = sb.toString().replaceAll("\"", "");
				sb.delete(0, sb.length());
			}
			else if (depth == 0 && charAt == ',' && !quote) { // end of value
				final String value = sb.toString();
				sb.delete(0, sb.length());
				keyToValue.put(key, value);
			}
			else {
				sb.append(charAt);
			}
		}
		final String value = sb.toString();
		keyToValue.put(key, value);
		return keyToValue;
	}

	/**
	 * As string.
	 * 
	 * @param key the key
	 * @param object the object
	 * @return the string
	 */
	public static String asString(final String key, final Map<String, String> object) {
		final String string = object.get(key);
		if (string == null) {
			return null;
		}
		return string.replaceAll("\"", "");
	}

	/**
	 * As string.
	 * 
	 * @param key the key
	 * @param object the object
	 * @return the string
	 */
	public static int asInteger(final String key, final Map<String, String> object) {
		final String value = object.get(key);
		if (value == null) {
			throw new IllegalStateException("No key " + key + " found");
		}
		return Integer.parseInt(value);
	}

	/**
	 * Convert the value map to a json string
	 * 
	 * @param object the object
	 * @return the string
	 */
	public static String toJsonString(final Map<String, Object> object) {
		final StringBuilder sb = new StringBuilder("{");
		int count = 0;
		for (final Map.Entry<String, Object> entry : object.entrySet()) {

			if (count > 0) {
				sb.append(",");
			}
			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\"");
			sb.append(":");

			appendValue(entry.getValue(), sb);
			count++;
		}
		sb.append("}");
		return sb.toString();
	}

	private static void appendValue(final Object value, final StringBuilder sb) {
		if (value instanceof List) {
			toJsonString((List<?>) value, sb);
		}
		else if (value instanceof String) {
			sb.append("\"");
			sb.append(value.toString());
			sb.append("\"");
		}
		else {
			sb.append(value.toString());
		}
	}

	private static void toJsonString(final List<?> array, final StringBuilder sb) {
		int count = 0;
		sb.append("[");
		for (final Object value : array) {
			if (count > 0) {
				sb.append(",");
			}
			appendValue(value, sb);
			count++;
		}
		sb.append("]");
	}

}
