package core.testdata.manager;

import java.util.ArrayList;

/**
 * @summary This POJO class contains attributes for a TESTCASE 
 * @author Surendra.Shekhawat
 *
 */
public class TestCase implements Cloneable {
	private String id;
	private String name;
	private String description;
	private String objectives;
	private String note;
	private boolean active;
	private String tag;
	private ArrayList<TestStep> _testSteps;
	private String testType;
	private String sector;
	private String module;
	private String feature;
	private String group = "default";
	private String category;
	private String battery;
	private String key;

	/**
	 * @Summary Constructor sets value of attributes to EMPTY from NULL
	 * @deprecated 
 	 */
	public TestCase() {
		setId("");
		setName("");
		setDescription("");
		setNote("");
		setTag("");
		setActive(true);
		setObjectives("");
		setTestType("");
		setSector("");
		setModule("");
		setFeature("");
		setCategory("");
		setBattery("");
		setKey("");
		set_testSteps(new ArrayList<TestStep>());
	}

	/**
	 * @summary Constructor sets value of attributes passed in it 
	 * @param id
	 * @param name
	 * @param description
	 */
	public TestCase(Object id, Object name, Object description) {
		setId(id);
		setName(name);
		setDescription(description);
		setNote("");
		setTag("");
		setActive(true);
		setObjectives("");
		setTestType("");
		setSector("");
		setModule("");
		setFeature("");
		setCategory("");
		setBattery("");
		set_testSteps(new ArrayList<TestStep>());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getTestType() {
		return testType;
	}

	public void setTestType(Object testType) {
		this.testType = (String) testType;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(Object sector) {
		this.sector = (String) sector;
	}

	public String getModule() {
		return module;
	}

	public void setModule(Object module) {
		this.module = (String) module;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(Object feature) {
		this.feature = (String) feature;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(Object group) {
		this.group = (String) group;
	}

	public String getId() {
		return id;
	}

	public void setId(Object id) {
		if (id instanceof String) {
			this.id = (String) id;
		} else
			this.id = "";
	}

	public String getName() {
		return name;
	}

	public void setName(Object name) {
		if (name instanceof String) {
			this.name = (String) name;
		} else
			this.name = "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		if (description instanceof String) {
			this.description = (String) description;
		} else
			this.description = "";
	}

	public ArrayList<TestStep> get_testSteps() {
		return _testSteps;
	}

	public void set_testSteps(ArrayList<TestStep> _testSteps) {
		this._testSteps = _testSteps;
	}

	public String getNote() {
		return note;
	}

	public void setNote(Object note) {
		if (note instanceof String) {
			this.note = (String) note;
		} else
			this.note = "";
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(Object active) {
		if (active instanceof Boolean) {
			this.active = (boolean) active;
		} else
			this.active = true;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		if (tag instanceof String) {
			this.tag = (String) tag;
		} else
			this.tag = "";
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(Object objectives) {
		if (objectives instanceof String) {
			this.objectives = (String) objectives;
		} else
			this.objectives = "";
	}

	/**
	 * @summary To reset an objective to EMPTY values
	 */
	public void clear() {
		id = "";
		name = "";
		description = "";
		active = true;
		tag = "";
		objectives = "";
		for (TestStep ts : _testSteps) {
			ts.clear();
		}
		testType = "";
		sector = "";
		module = "";
		feature = "";
		battery = "";
		category = "";
		group = "default";
		_testSteps.clear();
	}

	/**
	 * @summary 
	 * @deprecated
	 * @param stepOrder
	 * @param paramName
	 * @return
	 * TODO Find purpose of this method
	 */
	public Object getParamValueFromTestStep(int stepOrder, String paramName) {
		return get_testSteps().get(stepOrder - 1).getTestParams().get(paramName);
	}
}
