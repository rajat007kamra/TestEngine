package core.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @summary This file contains all methods related to JSON operations
 * @author Surendra.Shekhawat
 */
public class JsonHandler {
	final static Logger logger = Logger.getLogger(JsonHandler.class);
	public static JSONObject getDataFile(String fileName) {
		JSONObject testObject = null;

		try {
			// FileReader reader = new FileReader(filePath + fileName);
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(buff);
			testObject = jsonObject;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return testObject;
	}

	/**
	 * @summary Parse JSON from string
	 * @param jsonString
	 * @return
	 */
	public static JSONObject parseFromString(String jsonString) {
		JSONObject testObject = null;
		try {
			JSONParser jsonParser = new JSONParser();
			testObject = (JSONObject) jsonParser.parse(jsonString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return testObject;
	}

	/**
	 * @summary Prints JSON object on console
	 * @param jsonObj
	 */
	public static void printJsonObject(JSONObject jsonObj) {
		for (Object key : jsonObj.keySet()) {
			// based on you key types
			String keyStr = (String) key;
			Object keyvalue = jsonObj.get(keyStr);

			// Print key and value
			logger.info("key: " + keyStr + " value: " + keyvalue);

			// for nested objects iteration if required
			if (keyvalue instanceof JSONObject)
				printJsonObject((JSONObject) keyvalue);
		}
	}

	/**
	 * @summary 
	 * @deprecated
	 * TODO To be removed from repo as there is already a method in HashMapHandler
	 * @param jsObj
	 * @return
	 */
	public static LinkedHashMap<Integer, Object> GetObjectParamsFromJsonObject(JSONObject jsObj) {
		LinkedHashMap<Integer, Object> paramMap = new LinkedHashMap<Integer, Object>();
		int indexParam = 0;
		for (Object keyCParam : jsObj.keySet()) {
			indexParam++;
			paramMap.put(indexParam, keyCParam);
		}
		return paramMap;
	}

	/**
	 * @summary 
	 * @deprecated
	 * TODO To be removed from repo as there is already a method in HashMapHandler
	 * @param jsObj
	 * @return
	 */
	public static LinkedHashMap<String, Object> GetParamsFromJsonObject(JSONObject jsObj) {
		LinkedHashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();
		for (Object keyCParam : jsObj.keySet()) {
			paramMap.put((String) keyCParam, keyCParam);
		}
		return paramMap;
	}

	/**
	 * @summary This method is responsible for fetching JSON object from parent JSON object for an attribute
	 * @param parent
	 * @param objName
	 * @return
	 */
	public static JSONObject GetJSONObject(JSONObject parent, String objName) {
		JSONObject jObj = null;
		if (parent != null) {
			Object obj = parent.get(objName);
			jObj = (obj == null) ? null : (JSONObject) obj;
		}
		return jObj;
	}

	/**
	 * @summary This method is responsible for fetching JSON value from parent JSON object for an attribute
	 * @param parent
	 * @param objName
	 * @return
	 */
	public static Object GetValueJSONObject(JSONObject parent, String objName) {
		Object value = null;
		if (parent != null) {
			Object obj = parent.get(objName);
			value = (obj == null) ? null : obj;
		}
		return value;
	}

	/**
	 * @summary Method responsible for replacing map data for specified value against a pattern
	 * @param source
	 * @param beginPattern
	 * @param endPattern
	 * @param row
	 * @return
	 */
	public static JSONObject replaceValueByMapData(JSONObject source, String beginPattern, String endPattern,
			LinkedHashMap<String, Object> row) {
		JSONObject result = new JSONObject(source);
		for (Object key : source.keySet()) {
			if (source.get(key) instanceof String) {
				result.put(key,
						StringHandler.replaceValueByMapData((String) source.get(key), beginPattern, endPattern, row));
			}
			if (source.get(key) instanceof JSONObject) {
				result.put(key, replaceValueByMapData((JSONObject) source.get(key), beginPattern, endPattern, row));
			}
		}
		return result;
	}
}
