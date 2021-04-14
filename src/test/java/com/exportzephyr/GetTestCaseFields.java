package com.exportzephyr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class GetTestCaseFields {
	final static Logger logger = Logger.getLogger(GetTestCaseFields.class);
	public static String testCaseUrl = "https://app.tm4j.smartbear.com/backend/rest/tests/2.0/testcase/"; // TEST-T899
	public static String foldersUrl = "https://app.tm4j.smartbear.com/backend/rest/tests/2.0/project/10091/foldertree/testrun";
	public static String customFieldsUrl = "https://app.tm4j.smartbear.com/backend/rest/tests/2.0/project/10091/customfields/testcase";
	public static String issuesDetailsUrl = "https://mezocliq.atlassian.net/rest/api/3/issue/"; // P20-2622
	
//	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
//
//		getTestCaseFieldsValue(GetTestCycle.accessToken, "", "", "", "TEST-T899", "", "", "", 0);
//	}

	// Method to get Test Case fields value
	public static void getTestCaseFieldsValue(String accessToken, String folderName, String environment,
			String testCycleKey, String testCycleName, String testCaseKey, String testCaseName,
			String testCaseLastExecutionDate, int testCaseDefectCount) throws IOException, JSONException {
		int customFieldNameIntValue;
		int issuesCount = 0;
		int issueStatusCount = 0;
		int issuePriorityCount = 0;
		int openAllCount = 0;
		int openBlockerCount = 0;
		String testCaseType = null;
		String testCaseStatus = null;
		String testCaseMode = null;
		String testCaseCategory = null;
		String testCaseSubCategory = null;
		String testCaseIsAutomated = null;
		String testCaseBattery = null;
		String getLastTestResultStatus = null;

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Builder builder = new Request.Builder();

		Request request = new Request.Builder().header("Authorization", accessToken).url(testCaseUrl + testCaseKey)
				.build();
		Response response = client.newCall(request).execute();
		String actualJson = response.body().string();
//		logger.info(actualJson);

		StringBuilder sb = new StringBuilder(actualJson);
		sb.insert(0, "[");
		sb.insert(actualJson.length() + 1, "]");
		String output = sb.toString();
//		logger.info(output);

		JSONArray array1 = new JSONArray(output);

		for (int i = 0; i < array1.length(); i++) {
//			logger.info("\n " + array.getString(i));
			String s = array1.getString(i);
			JSONObject jsonObject = new JSONObject(s);

			JSONObject getStatus = jsonObject.getJSONObject("status");
			testCaseStatus = (String) getStatus.get("name");
			logger.info("TEST CASE STATUS = " + testCaseStatus);

			JSONObject getLastResultStatus = jsonObject.getJSONObject("lastTestResultStatus");
			getLastTestResultStatus = (String) getLastResultStatus.get("name");
			logger.info("TEST LAST RESULT STATUS = " + getLastTestResultStatus);
		}

		JSONObject jsonOb = new JSONObject(actualJson);
		JSONArray arrayIssues = jsonOb.getJSONArray("issueLinks");
		for (int j = 0; j < arrayIssues.length(); j++) {
			logger.info("\n " + arrayIssues.getString(j));

			issuesCount = arrayIssues.length();
			logger.info("ISSUES COUNT = " + issuesCount);

			JSONObject object = (JSONObject) arrayIssues.get(j);

			int issuesId = object.getInt("issueId");
			logger.info("ISSUES IDs = " + issuesId);

			issueStatusCount = getIssueStatus(issuesId);
			openAllCount = openAllCount + issueStatusCount;
			logger.info("STATUS COUNT = " + issueStatusCount);

			issuePriorityCount = getIssuePriority(issuesId);
			openBlockerCount = openBlockerCount + issuePriorityCount;
			logger.info("PRIORITY COUNT = " + issuePriorityCount);
		}
		
		JSONArray array = jsonOb.getJSONArray("customFieldValues");
		for (int j = 0; j < array.length(); j++) {
//			logger.info("\n " + array.getString(i));
			JSONObject object = (JSONObject) array.get(j);
			JSONObject getCustomField = object.getJSONObject("customField");
			String customFieldName = (String) getCustomField.get("name");
//			logger.info("Custom Field Name = " + customFieldName);

			if (customFieldName.equals("MODE")) {
				try {
					customFieldNameIntValue = object.getInt("intValue");
//					logger.info("Test Cycle ID  = " + customFieldNameIntValue);
					testCaseMode = getTestCaseCustomFieldsValue(accessToken, customFieldName, customFieldNameIntValue);
					logger.info("TEST CASE MODE = " + testCaseMode);
				} catch (Exception e) {
					continue;
				}
			} else if (customFieldName.equals("CATEGORY")) {
				try {
					customFieldNameIntValue = object.getInt("intValue");
//					logger.info("Test Cycle ID  = " + customFieldNameIntValue);
					testCaseCategory = getTestCaseCustomFieldsValue(accessToken, customFieldName,
							customFieldNameIntValue);
					logger.info("TEST CASE CATEGORY = " + testCaseCategory);
				} catch (Exception e) {
					continue;
				}
			} else if (customFieldName.equals("SUBCATEGORY")) {
				try {
					customFieldNameIntValue = object.getInt("intValue");
//					logger.info("Test Cycle ID  = " + customFieldNameIntValue);
					testCaseSubCategory = getTestCaseCustomFieldsValue(accessToken, customFieldName,
							customFieldNameIntValue);
					logger.info("TEST CASE SUBCATEGORY = " + testCaseSubCategory);
				} catch (Exception e) {
					continue;
				}
			} else if (customFieldName.equals("BATTERY")) {
				try {
					testCaseBattery = object.getString("stringValue");
					logger.info("TEST CASE BATTERY = " + testCaseBattery);
				} catch (Exception e) {
					continue;
				}
			} else if (customFieldName.equals("TYPE")) {
				try {
					customFieldNameIntValue = object.getInt("intValue");
//					logger.info("Test Cycle ID  = " + customFieldNameIntValue);
					testCaseType = getTestCaseCustomFieldsValue(accessToken, customFieldName, customFieldNameIntValue);
					logger.info("TEST CASE TYPE = " + testCaseType);
				} catch (Exception e) {
					continue;
				}
			} else if (customFieldName.equals("AUTOMATED?")) {
				try {
					customFieldNameIntValue = object.getInt("intValue");
//					logger.info("Test Cycle ID  = " + customFieldNameIntValue);
					testCaseIsAutomated = getTestCaseCustomFieldsValue(accessToken, customFieldName,
							customFieldNameIntValue);
					logger.info("TEST CASE AUTOMATED = " + testCaseIsAutomated);
				} catch (Exception e) {
					continue;
				}
			} else {

			}
		}
		WriteInExcel.addRowInExcelSheet(folderName, environment, testCaseLastExecutionDate, testCaseType,
				testCaseCategory, testCaseSubCategory, testCaseMode, testCaseBattery, testCaseIsAutomated, testCaseName,
				testCaseDefectCount, getLastTestResultStatus, String.valueOf(issuesCount), String.valueOf(openAllCount),
				String.valueOf(openBlockerCount), testCycleName);
	}

	// Method to get Test Cycle Folder Name using folder id
	public static String getFolderName(String accessToken, int folderId) throws IOException, JSONException {
		String folderName = null;
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Builder builder = new Request.Builder();

		// FOR TEST CYCLE - TEST CASES
		Request request = new Request.Builder().header("Authorization", accessToken).url(foldersUrl).build();
		Response response = client.newCall(request).execute();
		String actualJson = response.body().string();

		JSONObject jsonOb = new JSONObject(actualJson);

		JSONArray array = jsonOb.getJSONArray("children");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = (JSONObject) array.get(i);

			int testCycleFolderId = object.getInt("id");
			if (folderId == testCycleFolderId) {
				logger.info("FOLDER ID  = " + testCycleFolderId);
				folderName = object.getString("name");
				logger.info("FOLDER NAME  = " + folderName);
				break;
			}
			JSONArray arrJson1 = object.getJSONArray("children");
			for (int j = 0; j < arrJson1.length(); j++) {
				JSONObject object1 = (JSONObject) arrJson1.get(j);

				int testCycleSubFolderId = object1.getInt("id");
				if (folderId == testCycleSubFolderId) {
					logger.info("SUB FOLDER ID  = " + testCycleSubFolderId);
					folderName = object1.getString("name");
					logger.info("SUB FOLDER NAME  = " + folderName);
					break;
				}
			}
		}
		return folderName;
	}

	// Method to get Test Case fields value like Type, Mode, Category, SubCategory
	public static String getTestCaseCustomFieldsValue(String accessToken, String fieldName, int FieldNameOptionId)
			throws IOException, JSONException {
		String OptionName = null;
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Builder builder = new Request.Builder();

		Request request = new Request.Builder().header("Authorization", accessToken).url(customFieldsUrl).build();
		Response response = client.newCall(request).execute();
		String actualJson = response.body().string();
//		logger.info(actualJson);

		JSONArray array = new JSONArray(actualJson);

		for (int i = 0; i < array.length(); i++) {
//				logger.info("\n " + array.getString(i));
			String s = array.getString(i);

			JSONObject jsonObject = new JSONObject(s);

			String testName = (String) jsonObject.get("name");
			if (testName.equals(fieldName)) {
				logger.info("TEST NAME = " + testName);
				JSONArray optionsArray = jsonObject.getJSONArray("options");
				for (int j = 0; j < optionsArray.length(); j++) {
					JSONObject object = (JSONObject) optionsArray.get(j);

					int optionsId = object.getInt("id");
					if (optionsId == FieldNameOptionId) {
						logger.info("OPTION ID  = " + optionsId);
						OptionName = object.getString("name");
						logger.info("OPTION NAME  = " + OptionName);
						break;
					}
				}
			}
		}
		return OptionName;
	}

	// Method to fetch the bugs count which has OPEN status
	public static int getIssueStatus(int issueId) throws JSONException, IOException {
		String json = getIssuesDetails(issueId);
		int countStatus = 0;
		JSONArray array1;
		try {
			array1 = new JSONArray(json);
			for (int i = 0; i < array1.length(); i++) {
//					logger.info("\n " + array.getString(i));
				String s = array1.getString(i);
				JSONObject jsonObject = new JSONObject(s);

				JSONObject getFields = jsonObject.getJSONObject("fields");
				JSONObject getStatus = getFields.getJSONObject("status");
				String bugStatus = (String) getStatus.get("name");
				if (bugStatus.equals("Open")) {
					countStatus = countStatus + 1;
					logger.info("BUG STATUS = " + bugStatus + " and count = " + countStatus);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return countStatus;
	}

	//Method fetching bug details from Jira which linked with test cases in zephyr
		public static String getIssuesDetails(int IssueId) throws IOException {
			String output = null;
			try {
				URL url = new URL(issuesDetailsUrl + IssueId);
				logger.info("URL - " + url);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept-Language", "UTF-8");
				String user = "arun.kapoor@mezocliq.com";
				String pass = "P18M7lDIJ9xTpYZRayX0B3DE";
				String authStr = user + ":" + pass;
				String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
				con.setDoOutput(true);
				con.setRequestProperty("Authorization", "Basic " + authEncoded);
				con.connect();

				int responsecode = con.getResponseCode();
				if (responsecode != 200)
					throw new RuntimeException();
				else {
					InputStream content = (InputStream) con.getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = in.readLine()) != null) {
						logger.info(line);

						StringBuilder sb = new StringBuilder(line);
						sb.insert(0, "[");
						sb.insert(line.length() + 1, "]");
						output = sb.toString();
						logger.info(output);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return output;
		}
		
	// Method to fetch the bugs count which has HIGH OR CRITICAL priority
	public static int getIssuePriority(int issueId) throws JSONException, IOException {
		String json = getIssuesDetails(issueId);
		int countPriority = 0;
		JSONArray array1;
		try {
			array1 = new JSONArray(json);
			for (int i = 0; i < array1.length(); i++) {
//					logger.info("\n " + array.getString(i));
				String s = array1.getString(i);
				JSONObject jsonObject = new JSONObject(s);

				JSONObject getFields = jsonObject.getJSONObject("fields");
				JSONObject getStatus = getFields.getJSONObject("status");
				String bugStatus = (String) getStatus.get("name");
				if (bugStatus.equals("Open")) {
					JSONObject getPriority = getFields.getJSONObject("priority");
					String bugPriority = (String) getPriority.get("name");
					if (bugPriority.equals("1-Critical (Z)") || bugPriority.equals("2-High (A)")) {
						countPriority = countPriority + 1;
						logger.info("BUG PRIORITY = " + bugPriority);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return countPriority;
	}
}