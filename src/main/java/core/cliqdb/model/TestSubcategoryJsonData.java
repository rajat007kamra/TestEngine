package core.cliqdb.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */

public class TestSubcategoryJsonData {
	@SerializedName("suiteName")
	@Expose
	private String suiteName;
	@SerializedName("suiteDescription")
	@Expose
	private String suiteDescription;
	@SerializedName("note")
	@Expose
	private String note;
	@SerializedName("category")
	@Expose
	private String category;
	@SerializedName("battery")
	@Expose
	private String battery;
	@SerializedName("testCases")
	@Expose
	private List<TestcaseJsonData> testCases = null;
	
	@SerializedName("subCategory")
	@Expose
	private String subCategory;
	

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getSuiteDescription() {
		return suiteDescription;
	}

	public void setSuiteDescription(String suiteDescription) {
		this.suiteDescription = suiteDescription;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public List<TestcaseJsonData> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestcaseJsonData> testCases) {
		this.testCases = testCases;
	}
}
