package testNG.utils;

import java.util.LinkedList;

/**
 * @summary Model (POJO) class for handling data while generating testNG.xml
 * @author Surendra.Shekhawat
 */
public class SuiteIdentifiers {
	private String testNgFileName = "testNg.xml";
	private String suiteName;
	private String suiteParallel;
	private String testName;
	private boolean preserveOrder = true;
	private int verboseValue = 1;
	private int threadCount = 1;
	private boolean isParallel = false;
	private LinkedList<String> testCases;
	private String category;
	private String battery;
	private int thread_count;

	public int getThread_count() {
		return thread_count;
	}

	public void setThread_count(int thread_count) {
		this.thread_count = thread_count;
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

	public LinkedList<String> getTestCases() {
		return testCases;
	}

	public void setTestCases(LinkedList<String> testCases) {
		this.testCases = testCases;
	}

	public String getTestNgFileName() {
		return testNgFileName;
	}

	public void setTestNgFileName(String testNgFileName) {
		this.testNgFileName = testNgFileName;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getSuiteParallel() {
		return suiteParallel;
	}

	public void setSuiteParallel(String suiteParallel) {
		this.suiteParallel = suiteParallel;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public boolean isPreserveOrder() {
		return preserveOrder;
	}

	public void setPreserveOrder(boolean preserveOrder) {
		this.preserveOrder = preserveOrder;
	}

	public int getVerboseValue() {
		return verboseValue;
	}

	public void setVerboseValue(int verboseValue) {
		this.verboseValue = verboseValue;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public boolean isParallel() {
		return isParallel;
	}

	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}

}
