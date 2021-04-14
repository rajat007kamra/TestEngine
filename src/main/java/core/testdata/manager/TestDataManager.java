package core.testdata.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import core.utilities.JsonHandler;

/**
 * @summary This class is responsible to populate {@link TestCase.java}  
 * @author Surendra.Shekhawat
 *
 */
public class TestDataManager {
	protected String testDataFolder;
	protected LinkedHashMap<String, TestSuite> testSuiteMap;

	public String getTestDataFolder() {
		return testDataFolder;
	}

	public void setTestDataPath(String testDataFolder) {
		this.testDataFolder = testDataFolder;
	}

	public TestDataManager() {
		testSuiteMap = new LinkedHashMap<>();
	}

	public LinkedHashMap<String, TestSuite> getTestSuiteMap() {
		return testSuiteMap;
	}

	public void setTestData(String testDataPath) {
		testSuiteMap.put(testDataPath, loadTestDataFromJson(testDataPath));
	}

	public void clearTestSuiteMap() {
		for (String keyTS : testSuiteMap.keySet()) {
			testSuiteMap.get(keyTS).clear();
		}
		testSuiteMap.clear();
	}

	/**
	 * @summary This method is responsible for populating {@link TestCase.java} from test JSON file
	 * @param testPath
	 * @return
	 */
	public TestCase loadTestCaseFromJson(String testPath) {

		JSONObject jsonTestCase = JsonHandler.getDataFile(testPath);
		TestCase tc = new TestCase(jsonTestCase.get("testId"), jsonTestCase.get("testName"),
				jsonTestCase.get("testDescription"));
		tc.setObjectives(jsonTestCase.get("testObjectives"));
		tc.setNote(jsonTestCase.get("note"));
		tc.setActive(jsonTestCase.get("active"));
		tc.setTag(jsonTestCase.get("tag"));
		if (jsonTestCase.get("category") != null && jsonTestCase.get("battery") != null) {
			tc.setCategory(jsonTestCase.get("category").toString());
			tc.setBattery(jsonTestCase.get("battery").toString());
		} else {
			tc.setCategory("TBD");
			tc.setBattery("TBD");
		}
		JSONArray testSteps = ((JSONArray) jsonTestCase.get("actions"));
		for (int i = 0; i < testSteps.size(); i++) {
			JSONObject jsonTestStep = (JSONObject) testSteps.get(i);
			JSONObject params = (JSONObject) jsonTestStep.get("context");
			TestStep testStep = new TestStep((String) jsonTestStep.get("name"),
					(String) jsonTestStep.get("testDescription"), (String) jsonTestStep.get("class"),
					(String) jsonTestStep.get("method"), (String) jsonTestStep.get("report"));
			if (params != null) {
				testStep.getTestParams().putAll(params);
			}
			tc.get_testSteps().add(testStep);
		}

		return tc;
	}

	/**
	 * @summary This method is responsible for populating {@link TestSuite.java} from test JSON file
	 * @param testPath
	 * @return
	 */
	public TestSuite loadTestDataFromJson(String testPath) {
		TestSuite tSuite = new TestSuite();
		JSONObject jsonObj = JsonHandler.getDataFile(getTestDataFolder() + testPath);
		tSuite.set_name((String) jsonObj.get("suiteName"));
		tSuite.set_description((String) jsonObj.get("suiteDescription"));
		tSuite.set_note(jsonObj.get("note"));
		if (jsonObj.get("category") != null && jsonObj.get("battery") != null) {
			tSuite.setCategory(jsonObj.get("category").toString());
			tSuite.setBattery(jsonObj.get("battery").toString());
		} else {
			tSuite.setCategory("TBD");
			tSuite.setBattery("TBD");
		}

		JSONArray testCases = ((JSONArray) JsonHandler.GetValueJSONObject(jsonObj, "testCases"));
		if (testCases != null) {

			for (int i = 1; i <= testCases.size(); i++) {
				String jsonTestPath = (String) testCases.get(i - 1);
				tSuite.get_testCases().add(loadTestCaseFromJson(jsonTestPath));
			}
		}

		return tSuite;
	}

	/**
	 * @summary  This method is responsible for populating {@link TestSuite.java} from test JSON file.
	 * Applicable for multiple batteries
	 * @param testPath
	 * @return
	 */
	public List<TestSuite> loadTestDataFromJsonMultipleSuites(String testPath) {
		List<TestSuite> suiteList = new ArrayList<TestSuite>();
		JSONObject jsonObj = JsonHandler.getDataFile(getTestDataFolder() + testPath);
		JSONArray suites = (JSONArray) JsonHandler.GetValueJSONObject(jsonObj, "suites");
		if (suites != null) {
			for (int i = 1; i <= suites.size(); i++) {
				JSONObject suiteObject = (JSONObject) suites.get(i - 1);
				TestSuite tSuite = new TestSuite();
				tSuite.set_name((String) suiteObject.get("suiteName"));
				tSuite.set_description((String) suiteObject.get("suiteDescription"));
				tSuite.set_note(suiteObject.get("note"));

				if (suiteObject.get("category") != null && suiteObject.get("battery") != null) {
					tSuite.setCategory(suiteObject.get("category").toString());
					tSuite.setBattery(suiteObject.get("battery").toString());
				} else {
					tSuite.setCategory("TBD");
					tSuite.setBattery("TBD");
				}

				JSONArray testCases = ((JSONArray) JsonHandler.GetValueJSONObject(suiteObject, "testCases"));
				if (testCases != null) {

					for (int j = 1; j <= testCases.size(); j++) {
						String jsonTestPath = testCases.get(j - 1).toString();
						tSuite.get_testCases().add(loadTestCaseFromJson(jsonTestPath));
					}
				}
				suiteList.add(tSuite);
			}
		}
		return suiteList;
	}

	/**
	 * @summary  This method is responsible for populating {@link TestSuite.java} from test JSON string.
	 * Applicable for multiple batteries
	 * @param testPath
	 * @return
	 */
	public List<TestSuite> loadTestDataFromJsonMultipleSuitesJson(String suiteJson) throws ParseException {
		List<TestSuite> suiteList = new ArrayList<TestSuite>();
		JSONObject jsonObj = JsonHandler.parseFromString(suiteJson);
		JSONArray suites = (JSONArray) JsonHandler.GetValueJSONObject(jsonObj, "suites");
		if (suites != null) {
			for (int i = 1; i <= suites.size(); i++) {
				JSONObject suiteObject = (JSONObject) suites.get(i - 1);
				TestSuite tSuite = new TestSuite();
				tSuite.set_name((String) suiteObject.get("suiteName"));
				tSuite.set_description((String) suiteObject.get("suiteDescription"));
				tSuite.set_note(suiteObject.get("note"));

				if (suiteObject.get("category") != null && suiteObject.get("battery") != null) {
					tSuite.setCategory(suiteObject.get("category").toString());
					tSuite.setBattery(suiteObject.get("battery").toString());
				} else {
					tSuite.setCategory("TBD");
					tSuite.setBattery("TBD");
				}

				JSONArray testCases = ((JSONArray) JsonHandler.GetValueJSONObject(suiteObject, "testCases"));
				if (testCases != null) {

					for (int j = 1; j <= testCases.size(); j++) {
						String jsonTestPath = testCases.get(j - 1).toString();
						tSuite.get_testCases().add(loadTestCaseFromJsonString(jsonTestPath));
					}
				}
				suiteList.add(tSuite);
			}
		}
		return suiteList;
	}

	/**
	 * @summary  This method is responsible for populating {@link TestCase.java} from test JSON string.
	 * Applicable for multiple batteries
	 * @param testPath
	 * @return
	 */
	public TestCase loadTestCaseFromJsonString(String testJson) throws ParseException {

		JSONObject jsonTestCase = JsonHandler.parseFromString(testJson);
		TestCase tc = new TestCase(jsonTestCase.get("testId"), jsonTestCase.get("testName"),
				jsonTestCase.get("testDescription"));
		tc.setObjectives(jsonTestCase.get("testObjectives"));
		tc.setNote(jsonTestCase.get("note"));
		tc.setActive(jsonTestCase.get("active"));
		tc.setTag(jsonTestCase.get("tag"));
		tc.setKey(jsonTestCase.get("testKey").toString());
		if (jsonTestCase.get("category") != null && jsonTestCase.get("battery") != null) {
			tc.setCategory(jsonTestCase.get("category").toString());
			tc.setBattery(jsonTestCase.get("battery").toString());
		} else {
			tc.setCategory("TBD");
			tc.setBattery("TBD");
		}
		JSONArray testSteps = ((JSONArray) jsonTestCase.get("actions"));
		for (int i = 0; i < testSteps.size(); i++) {
			JSONObject jsonTestStep = (JSONObject) testSteps.get(i);
			JSONObject params = null;
			if (jsonTestStep.get("context") != null) {
				// Object parse = new JSONParser().parse(jsonTestStep.get("context").toString());
				params =  (JSONObject)jsonTestStep.get("context");
			}
			TestStep testStep = new TestStep((String) jsonTestStep.get("name"),
					(String) jsonTestStep.get("testDescription"), (String) jsonTestStep.get("class"),
					(String) jsonTestStep.get("method"), (String) jsonTestStep.get("report"));
			if (params != null) {
				testStep.getTestParams().putAll(params);
			}
			tc.get_testSteps().add(testStep);
		}

		return tc;
	}

}
