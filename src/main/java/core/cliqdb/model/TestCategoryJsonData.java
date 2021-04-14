	package core.cliqdb.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
 
public class TestCategoryJsonData {
	@SerializedName("suites")
	@Expose
	private List<TestSubcategoryJsonData> subCategoryTestcases = null;

	public List<TestSubcategoryJsonData> getSubcategoryTestCases() {
		return subCategoryTestcases;
	}

	public void setSubcategoryTestCases(List<TestSubcategoryJsonData> suites) {
		this.subCategoryTestcases = suites;
	}
}
