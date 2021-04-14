package core.cliqdb.model;

/**
 * 
 * @author Surendra.Shekhawat
 *
 */
public class TestcaseActionJsonData 
{
	private String name;
	private Object context;

	public TestcaseActionJsonData() 
	{		
	}
	
	public TestcaseActionJsonData(String name) 
	{
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}

}
