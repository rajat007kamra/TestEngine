package core.cliqdb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class TestCaseData {
	private ProfileData profileData;
	private SummaryData summaryData;
	private AttributeData attributeData;
	private String testName;
	private String enterpriseId;
	private String rowKey;
	private List<StepData> steps;
	private String caption;
	private String state;
	private String activationDate;
	private List<String> labels = new ArrayList<>();
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	public ProfileData getProfileData() {
		return profileData;
	}

	public void setProfileData(ProfileData profileData) {
		this.profileData = profileData;
	}

	public SummaryData getSummaryData() {
		return summaryData;
	}

	public void setSummaryData(SummaryData summaryData) {
		this.summaryData = summaryData;
	}

	public AttributeData getAttributeData() {
		return attributeData;
	}

	public void setAttributeData(AttributeData attributeData) {
		this.attributeData = attributeData;
	}

	public List<StepData> getSteps() {
		return steps;
	}

	public void setSteps(List<StepData> steps) {
		this.steps = steps;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
