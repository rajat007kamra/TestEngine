package com.tm4j.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tm4j.pojo.AllTestsData;
import com.tm4j.pojo.Tm4JTestCaseData;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class TM4JApiHelper {

	/**
	 * 
	 * @param projectID
	 * @return
	 * @throws IOException
	 */
	public static AllTestsData getAllTestCases(String projectKey) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(String.format(Tm4jConstatnts.getAllTestCase, projectKey));
		con.setRequestMethod("GET");

		return new Gson().fromJson(BaseTm4j.readResponse(con), AllTestsData.class);

	}

	/**
	 * 
	 * @param testKey
	 * @return
	 * @throws IOException
	 */
	public static Tm4JTestCaseData getTestCase(String testKey) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(String.format(Tm4jConstatnts.getTestCase, testKey));
		con.setRequestMethod("GET");
		String response = BaseTm4j.readResponse(con);
		con.disconnect();
		return new Gson().fromJson(response, Tm4JTestCaseData.class);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JsonElement createTest(String request) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(Tm4jConstatnts.createTestCase);
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		os.close();
		String response = BaseTm4j.readResponse(con);
		con.disconnect();

		return new Gson().fromJson(response, JsonElement.class);

	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Tm4JTestCaseData updateTestCases(String request, String testKey) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(String.format(Tm4jConstatnts.updateTestCase, testKey));
		con.setRequestMethod("PUT");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		os.close();
		String response = BaseTm4j.readResponse(con);
		con.disconnect();

		return new Gson().fromJson(response, Tm4JTestCaseData.class);
	}

	public static JsonElement createCycle(String request) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(Tm4jConstatnts.createCycle);
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		os.close();
		String response = BaseTm4j.readResponse(con);
		con.disconnect();

		return new Gson().fromJson(response, JsonElement.class);
	}

	public static JsonElement updateTestResult(String request) throws IOException {
		HttpURLConnection con = BaseTm4j.setUp(Tm4jConstatnts.createTestExecution);
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(request.getBytes());
		os.flush();
		os.close();
		String response = BaseTm4j.readResponse(con);
		con.disconnect();
		return new Gson().fromJson(response, JsonElement.class);
	}
}
