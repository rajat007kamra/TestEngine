package com.exportzephyr;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sharepoint.upload.SharepointOrchestrator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class GetTestCycle {
	final static Logger logger = Logger.getLogger(GetTestCycle.class);
	public static String testCycleUrl = "https://app.tm4j.smartbear.com/backend/rest/tests/2.0/testrun/search?maxResults=40&query=testRun.projectId+IN+(10091)+AND+testRun.folderTreeId+%3D+1148040&startAt=0";
	public static String testCycleTestCasesUrl = "https://app.tm4j.smartbear.com/backend/rest/tests/2.0/testrun/";
	public static String accessToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxYjAxYmZlNS02NTZkLTMwYmItODVmOC0zZmYzMzc1MzRlMWQiLCJjb250ZXh0Ijp7ImJhc2VVcmwiOiJodHRwczpcL1wvbWV6b2NsaXEuYXRsYXNzaWFuLm5ldCIsInVzZXIiOnsiYWNjb3VudElkIjoiNWUyNmViYmJlZTI2NGIwZTc0NThhMjU1In19LCJpc3MiOiJjb20ua2Fub2FoLnRlc3QtbWFuYWdlciIsImV4cCI6MTYzODYxNDk3MSwiaWF0IjoxNjA3MDc4OTcxfQ.LW8MywnULXKGuOu_JtefRNN7lVFuohc3N2_zK51AJVo";
	
	public static String folderName = "LEGAL ENTITY MANAGEMENT";
	public static String environment = "CLIQ-20";
	
	public static void main(String[] args) throws IOException, JSONException, InterruptedException 
	{
		CreateExcel.createWorkbook();
		getTestCycles();
		// upload a file to SharePoint site
        SharepointOrchestrator uploadToSharePoint = new SharepointOrchestrator();
        uploadToSharePoint.uploadDocument();
	}
	
	//Method to get all Test Cycles
	public static void getTestCycles() throws IOException, JSONException
	{
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Builder builder = new Request.Builder();

		// FOR TEST CYCLE
		Request request = new Request.Builder().header("Authorization", accessToken).url(testCycleUrl).build();
		Response response = client.newCall(request).execute();
		String actualJson = response.body().string();
		logger.info(actualJson);

		JSONObject jsonOb = new JSONObject(actualJson);
//		JSONArray arrJson = jsonOb.getJSONArray("results");
//		for (int i = 0; i < arrJson.length(); i++) {
//			logger.info("\n " + arrJson.getString(i));
//		}
		
		JSONArray array = jsonOb.getJSONArray("results");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = (JSONObject) array.get(i);

			//Getting Test Cycle Id
			int testCycleId = object.getInt("id");
			logger.info("Test Cycle ID  = " + testCycleId);
			
			//Getting Test Cycle Folder Id and name
//			int testCycleFolderId = object.getInt("folderId");
//			logger.info("Test Folder ID  = " + testCycleFolderId);			
//			String folderName = GetTestCaseFields.getFolderName(accessToken, testCycleFolderId);
//			logger.info("FOLDER NAME = " +folderName);
//			String[] splitFolderName = folderName.split("-"); // String array, each element is text between dots
//		    String newFolderName = splitFolderName[1];
//		    logger.info("NEW FOLDER NAME = " +newFolderName);
			
		    //Getting Test Cycle Id
			int testCycleVersionId = object.getInt("projectVersionId");
			logger.info("Test Cycle Version Id  = " + testCycleVersionId);
			
		    //Getting Test Cycle Key
			String testCycleKey = object.getString("key");
			logger.info("Test Cycle Key = " + testCycleKey);
		    
			//Getting Test Cycle Name
			String testCycleName = object.getString("name");
			logger.info("Test Cycle Name  = " + testCycleName);

			//Passing test cycle fields for fetching related test cases
			getTestCycleTestCases(folderName, environment, testCycleId, testCycleKey, testCycleName);
			logger.info("ALL ZEPHRY DATA FETCHED SUCCESSFULLY");
		}
	}
	
	//Methods to get test cases of each Test Cycle
	public static void getTestCycleTestCases(String folderName, String environment, int testCycleId, String testCycleKey, String testCycleName) throws IOException, JSONException
	{
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Builder builder = new Request.Builder();
		
		// FOR TEST CYCLE - TEST CASES
		Request request = new Request.Builder().header("Authorization", accessToken).url(testCycleTestCasesUrl +testCycleId +"/testrunitems").build();
		Response response = client.newCall(request).execute();
		String actualJson = response.body().string();
		JSONArray array = new JSONArray(actualJson);
		
		for (int i = 0; i < array.length(); i++) {
//			logger.info("\n " + array.getString(i));
			String s = array.getString(i);
			
			JSONObject jsonObject = new JSONObject(s);
		    JSONObject getChild = jsonObject.getJSONObject("$lastTestResult");
		    JSONObject getSubChild = getChild.getJSONObject("testCase");
			
		    int testIssueCount = (int) jsonObject.get("issueCount");
		    logger.info("ISSUE COUNT = " +testIssueCount); 
		    
		    String testKey = (String) getSubChild.get("key");
		    logger.info("TEST KEY = " +testKey);
		    
		    String testName = (String) getSubChild.get("name");
		    logger.info("TEST NAME = " +testName);
		    
		    String executionDate = (String) getChild.get("executionDate");
		    logger.info("TEST EXECUTION DATE = " +executionDate);
		    String[] splitExecutionDate = executionDate.split("T"); // String array, each element is text between dots
		    String onlyExecutionDate = splitExecutionDate[0];
		    logger.info("ONLY DATE = " +onlyExecutionDate);
		    
		    GetTestCaseFields.getTestCaseFieldsValue(accessToken, folderName, environment, testCycleKey, testCycleName, testKey, testName, onlyExecutionDate, testIssueCount);
		}
	}
}