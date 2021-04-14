
package com.tm4j.pojo.updatecycle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCycleData {

	@SerializedName("projectKey")
	@Expose
	private String projectKey;
	@SerializedName("testCaseKey")
	@Expose
	private String testCaseKey;
	@SerializedName("testCycleKey")
	@Expose
	private String testCycleKey;
	@SerializedName("statusName")
	@Expose
	private String statusName;
	@SerializedName("testScriptResults")
	@Expose
	private List<TestScriptResult> testScriptResults = null;
	@SerializedName("environmentName")
	@Expose
	private String environmentName;
	@SerializedName("actualEndDate")
	@Expose
	private String actualEndDate;
	@SerializedName("executionTime")
	@Expose
	private Integer executionTime;
	@SerializedName("executedById")
	@Expose
	private String executedById;
	@SerializedName("assignedToId")
	@Expose
	private String assignedToId;
	@SerializedName("comment")
	@Expose
	private String comment;
	@SerializedName("customFields")
	@Expose
	private CustomFields customFields;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getTestCaseKey() {
		return testCaseKey;
	}

	public void setTestCaseKey(String testCaseKey) {
		this.testCaseKey = testCaseKey;
	}

	public String getTestCycleKey() {
		return testCycleKey;
	}

	public void setTestCycleKey(String testCycleKey) {
		this.testCycleKey = testCycleKey;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<TestScriptResult> getTestScriptResults() {
		return testScriptResults;
	}

	public void setTestScriptResults(List<TestScriptResult> testScriptResults) {
		this.testScriptResults = testScriptResults;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}

	public String getExecutedById() {
		return executedById;
	}

	public void setExecutedById(String executedById) {
		this.executedById = executedById;
	}

	public String getAssignedToId() {
		return assignedToId;
	}

	public void setAssignedToId(String assignedToId) {
		this.assignedToId = assignedToId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

}
