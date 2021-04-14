
package com.tm4j.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tm4JTestCaseData {

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("projectKey")
	@Expose
	private String projectKey;
	@SerializedName("key")
	@Expose
	private String key;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("objective")
	@Expose
	private String objective;
	@SerializedName("precondition")
	@Expose
	private String precondition;
	@SerializedName("estimatedTime")
	@Expose
	private Integer estimatedTime;
	@SerializedName("componentId")
	@Expose
	private Integer componentId;
	@SerializedName("priorityName")
	@Expose
	private String priorityName;
	@SerializedName("statusName")
	@Expose
	private String statusName;
	@SerializedName("folderId")
	@Expose
	private Integer folderId;
	@SerializedName("ownerId")
	@Expose
	private String ownerId;
	@SerializedName("labels")
	@Expose
	private List<String> labels = null;
	@SerializedName("customFields")
	@Expose
	private CustomFields customFields;

	@SerializedName("project")
	@Expose
	private Project project;
	@SerializedName("priority")
	@Expose
	private Priority priority;
	@SerializedName("status")
	@Expose
	private Status status;
	@SerializedName("owner")
	@Expose
	private Owner owner;
	@SerializedName("testScript")
	@Expose
	private TestScript testScript;
	@SerializedName("folder")
	@Expose
	private Folder folder;

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public TestScript getTestScript() {
		return testScript;
	}

	public void setTestScript(TestScript testScript) {
		this.testScript = testScript;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

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

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setPrecondition(String precondition) {
		this.precondition = precondition;
	}

	public Integer getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Integer estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
