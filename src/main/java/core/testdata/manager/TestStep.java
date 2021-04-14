package core.testdata.manager;

import java.util.LinkedHashMap;

/**
 * @summary This is a POJO class responsible for test action information e.g. LOGIN 
 * @author Surendra.Shekhawat
 */
public class TestStep {

	private String name;
	private String description;
	private String classExecution;
	private String method;
	private String report;
	private LinkedHashMap<String, Object> testParams;

	public TestStep() {
		setName("");
		setDescription("");
		setClassExecution("");
		setMethod("");
		setTestParams(new LinkedHashMap<String, Object>());
	}

	public TestStep(String _name, String _description, String _classPath, String _method, String report) {
		setName(_name);
		setDescription(_description);
		setClassExecution(_classPath);
		setMethod(_method);
		setReport(report);
		setTestParams(new LinkedHashMap<String, Object>());
	}

	public TestStep(TestStep action) {
		this(action.getName(), action.getDescription(), action.getClassExecution(), action.getMethod(),
				action.getReport());
		testParams = new LinkedHashMap<String, Object>();
		for (String key : action.getTestParams().keySet()) {
			testParams.put(key, action.getTestParams().get(key));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		this.name = _name == null ? "" : _name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public LinkedHashMap<String, Object> getTestParams() {
		return testParams;
	}

	public void setTestParams(LinkedHashMap<String, Object> _testParams) {
		this.testParams = _testParams;
	}

	public String getClassExecution() {
		return classExecution;
	}

	public void setClassExecution(String _classPath) {
		this.classExecution = _classPath;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void clear() {
		name = "";
		description = "";
		method = "";
		classExecution = "";
		report = "";
		testParams.clear();
	}
}
