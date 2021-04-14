package com.tm4j.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class TestsExecutionData {
	@SerializedName("projectKey")
	@Expose
	private String projectKey;
	@SerializedName("testCycleKey")
	@Expose
	private String testCycleKey;
	@SerializedName("testCases")
	@Expose
	private List<String> testCases = null;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public List<String> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<String> testCases) {
		this.testCases = testCases;
	}

	public String getTestCycleKey() {
		return testCycleKey;
	}

	public void setTestCycleKey(String testCycleKey) {
		this.testCycleKey = testCycleKey;
	}

}
