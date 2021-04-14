package core.cliqdb.model;

import java.util.List;

public class StepData {

	private Integer stepSequenceNumber;
	private String stepName;
	private String stepType;
	private String count;
	private String recordType;
	private String user;	
	private List<SectionData> stepSections;

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<SectionData> getStepSections() {
		return stepSections;
	}

	public void setStepSections(List<SectionData> section) {
		this.stepSections = section;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public Integer getStepSequenceNumber() {
		return stepSequenceNumber;
	}

	public void setStepSequenceNumber(Integer stepSequenceNumber) {
		this.stepSequenceNumber = stepSequenceNumber;
	}

}
