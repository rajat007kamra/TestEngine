
package com.tm4j.pojo.createcycle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tm4j.pojo.updatecycle.CustomFields;

public class CreateCycleData {

	@SerializedName("projectKey")
	@Expose
	private String projectKey;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("plannedStartDate")
	@Expose
	private String plannedStartDate;
	@SerializedName("plannedEndDate")
	@Expose
	private String plannedEndDate;
	@SerializedName("jiraProjectVersion")
	@Expose
	private Integer jiraProjectVersion;
	@SerializedName("statusName")
	@Expose
	private String statusName;
	@SerializedName("folderId")
	@Expose
	private Integer folderId;
	@SerializedName("ownerId")
	@Expose
	private String ownerId;
	@SerializedName("customFields")
	@Expose
	private CustomFields customFields;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public String getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Integer getJiraProjectVersion() {
		return jiraProjectVersion;
	}

	public void setJiraProjectVersion(Integer jiraProjectVersion) {
		this.jiraProjectVersion = jiraProjectVersion;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getFolderId() {
		return folderId;
	}

	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

}
