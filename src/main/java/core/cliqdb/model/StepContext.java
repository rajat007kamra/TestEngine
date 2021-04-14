package core.cliqdb.model;

public class StepContext 
{
	private ProfileData profileData;
	private StepData stepData;
	private boolean isFormOpen;
	private boolean addLoginAction = false;

	public StepContext()
	{
		
	}
	
	public StepContext(ProfileData profileData, StepData stepData) {
		super();
		this.profileData = profileData;
		this.stepData = stepData;
	}
	
	public StepContext(ProfileData profileData, StepData stepData, boolean addLoginAction) {
		super();
		this.profileData = profileData;
		this.stepData = stepData;
		this.setAddLoginAction(addLoginAction);
	}
	
	public boolean isFormOpen() {
		return isFormOpen;
	}

	public void setFormOpen(boolean isFormOpen) {
		this.isFormOpen = isFormOpen;
	}

	public ProfileData getProfileData() 
	{
		return profileData;
	}
	
	public void setProfileData(ProfileData profileData) 
	{
		this.profileData = profileData;
	}
	
	public StepData getStepData() 
	{
		return stepData;
	}
	
	public void setStepData(StepData stepData) 
	{
		this.stepData = stepData;
	}

	public boolean isAddLoginAction() {
		return addLoginAction;
	}

	public void setAddLoginAction(boolean addLoginAction) {
		this.addLoginAction = addLoginAction;
	}
	
	
}
