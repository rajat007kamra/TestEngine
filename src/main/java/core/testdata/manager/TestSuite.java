package core.testdata.manager;

import java.util.ArrayList;

/**
 * @summary This is a POJO class responsible for test suite
 * @author Surendra.Shekhawat
 *
 */
public class TestSuite {
	private String _name;
	private String _description;
	private ArrayList<TestCase> _testCases;
	private ArrayList<TestSuite> _testSuites;
	private String _note;
	private String category;
	private String battery;
	private String subCategory;
	private boolean _parallelByGroup = false;

	public TestSuite() {
		_name = "";
		_description = "";
		_note = "";
		category = "";
		battery = "";
		subCategory = "";
		_testCases = new ArrayList<TestCase>();
		_testSuites = new ArrayList<>();
		_parallelByGroup = false;
	}

	
	public String getSubCategory() {
		return subCategory;
	}


	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
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

	public boolean is_parallelByGroup() {
		return _parallelByGroup;
	}

	public void set_parallelByGroup(boolean _parallelByGroup) {
		this._parallelByGroup = _parallelByGroup;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public ArrayList<TestCase> get_testCases() {
		return _testCases;
	}

	public void set_testCases(ArrayList<TestCase> _testCases) {
		this._testCases = _testCases;
	}

	public void clear() {
		_name = "";
		_description = "";
		category ="";
		battery ="";
		for (TestCase tc : _testCases) {
			tc.clear();
		}
		for (TestSuite ts : _testSuites) {
			ts.clear();
		}
		_testCases.clear();
		_testSuites.clear();
	}

	public ArrayList<TestSuite> get_testSuites() {
		return _testSuites;
	}

	public void set_testSuites(ArrayList<TestSuite> _testSuites) {
		this._testSuites = _testSuites;
	}

	public String get_note() {
		return _note;
	}

	public void set_note(Object _note) {
		if (_note instanceof String) {
			this._note = (String) _note;
		} else
			this._note = "";
	}
}
