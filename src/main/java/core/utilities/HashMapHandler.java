package core.utilities;

import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

/**
 * @summary Class which provides handler for handling HashMap. Ideally we should be using this instead of HashMap provided by Java.
 * @author Surendra.Shekhawat
 */
public class HashMapHandler {
	
	/**
	 * @summary Converts an array of json objects to hash map
	 * @param obj
	 * @param startIndex
	 * @return
	 */
	public static LinkedHashMap<Integer, Object> CreateHashMapValueWithKeyIndex(Object[] obj, int startIndex) {
		LinkedHashMap<Integer, Object> indexHashMap = new LinkedHashMap<Integer, Object>();
		for (int i = startIndex; i <= obj.length; i++) {
			indexHashMap.put(i, obj[i - startIndex]);
		}
		return indexHashMap;
	}

	/**
	 * @summary Converts any json object to hash map
	 * @param obj
	 * @param startIndex
	 * @return
	 */
	public static LinkedHashMap<String, Object> ConvertJsonObjectToHashMap(JSONObject jsonObj) {
		LinkedHashMap<String, Object> mapParams = new LinkedHashMap<String, Object>();
		if (jsonObj != null) {
			for (Object keyParam : jsonObj.keySet()) {
				mapParams.put((String) keyParam, jsonObj.get(keyParam));
			}
		}
		return mapParams;
	}

	/**
	 * @summary Replaces value of a source hash map to target hash map which starts and ends with a specific pattern 
	 * @param source
	 * @param beginPattern
	 * @param endPattern
	 * @param map
	 * @return
	 */
	public static LinkedHashMap<String, Object> replaceValueByMapData(LinkedHashMap<String, Object> source,
			String beginPattern, String endPattern, LinkedHashMap<String, Object> map) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>(source);
		for (String key : source.keySet()) {
			if (source.get(key) instanceof String) {
				result.put(key,
						StringHandler.replaceValueByMapData((String) source.get(key), beginPattern, endPattern, map));
			}
			if (source.get(key) instanceof LinkedHashMap) {
				result.put(key, replaceValueByMapData((LinkedHashMap<String, Object>) source.get(key), beginPattern,
						endPattern, map));
			}
			if (source.get(key) instanceof JSONObject) {
				result.put(key,
						JsonHandler.replaceValueByMapData((JSONObject) source.get(key), beginPattern, endPattern, map));
			}
		}
		return result;
	}
}
