package core.cliqdb.model;

import java.util.List;
import java.util.Map;

public class SectionData {
	private String name;
	private List<Map<String, String>> sectionContext;
	private Map<String, Map<String, String>> screenData;
	private Map<String, String> mzoneData;
	private Map<String, String> simpleName;
	private String processName;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Map<String, String>> getSectionContext() {
		return sectionContext;
	}

	public void setSectionContext(List<Map<String, String>> sectionContext) {
		this.sectionContext = sectionContext;
	}

	public Map<String, Map<String, String>> getScreenData() {
		return screenData;
	}

	public void setScreenData(Map<String, Map<String, String>> screenData) {
		this.screenData = screenData;
	}

	public Map<String, String> getMzoneData() {
		return mzoneData;
	}

	public void setMzoneData(Map<String, String> mzoneData) {
		this.mzoneData = mzoneData;
	}

	public Map<String, String> getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(Map<String, String> simpleName) {
		this.simpleName = simpleName;
	}

}
