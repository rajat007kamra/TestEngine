package com.tm4j.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTestsData {
	@SerializedName("values")
	@Expose
	private List<Tm4JTestCaseData> testcasedata = null;

	public List<Tm4JTestCaseData> getTestCases() {
		return testcasedata;
	}

	public void setTestCase(List<Tm4JTestCaseData> testcasedata) {
		this.testcasedata = testcasedata;
	}
}
