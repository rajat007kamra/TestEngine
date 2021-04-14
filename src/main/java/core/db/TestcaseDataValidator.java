package core.db;

import java.util.Map;

import org.apache.log4j.Logger;

import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepData;
import core.cliqdb.model.TestCaseData;

public class TestcaseDataValidator 
{
	final static Logger logger = Logger.getLogger(TestcaseDataValidator.class);
	// Validate JSON data and add label JOSN:INVALID as needed
	public static void validateTestcaseData(TestCaseData testcaseData) 
	{		
		Boolean isValidJson = Boolean.parseBoolean("true");
		if (DataExtractor.isEmpty(testcaseData.getProfileData().getCategory()) || DataExtractor.isEmpty(testcaseData.getProfileData().getSubcategory()))
		{
			logger.info("TESTCASE-INVALID: category or subcategory empty " + testcaseData.getEnterpriseId());
			isValidJson = false;
		}
		if (!DataExtractor.isEmpty(testcaseData.getSteps()))
		{
			for (StepData stepData : testcaseData.getSteps())
			{
				for (SectionData sectionData : stepData.getStepSections())
				{
					if ("CHECK-MESSAGE".equals(sectionData.getName()))
					{
						isValidJson = isValidJson && validateSectionData(sectionData, "MESSAGE", null);
					}
					else if ("CHECK-VARIABLE".equals(sectionData.getName()))
					{
						isValidJson = isValidJson && validateSectionData(sectionData, "VARIABLE", "VALUE");
					}
					else if ("FILTER".equals(sectionData.getName()))
					{						
						boolean isValidData = validateSectionData(sectionData, "RECORD CAPTION", "UI FIELD");
						if (!isValidData)
						{
							isValidData = validateSectionData(sectionData, "VALUE", "UI FIELD");
						}
						if (!isValidData)
						{
							isValidData = validateSectionData(sectionData, "VARIABLE", "VALUE");
						}
						if (!isValidData)
						{
							isValidData = validateSectionData(sectionData, "RECORD CAPTION", "VARIABLE");
						}
						if (!isValidData)
						{
							logger.info("TESTCASE-INVALID: Incomplete Data for " + sectionData.getName() + " StepName " + stepData.getStepName());
							isValidJson = false;
						}
					}
				}			
			}	
		}
		
		if (!isValidJson || DataExtractor.isEmpty(testcaseData.getSteps()))	
		{
			testcaseData.getLabels().add(CommonConstants.LABEL_INVALIDJSON);
			logger.info("TESTCASE-INVALID: Adding Label for ZID - "  + testcaseData.getEnterpriseId() + " *****************************");
		}
	}

	private static boolean validateSectionData(SectionData sectionData, String primaryLookupData, String secondaryLookupData) 
	{
		boolean addValidJsonTag = true;
		if (DataExtractor.isEmpty(sectionData.getSectionContext()))
		{
			logger.info("TESTCASE-INVALID: SectionContextData empty for " + sectionData.getName());			
			addValidJsonTag = false;
		}
		else
		{
			
			boolean foundData = false;
			for (Map<String, String> sectionDataMap : sectionData.getSectionContext())
			{
				if (!DataExtractor.isEmpty(primaryLookupData) && !DataExtractor.isEmpty(secondaryLookupData))
				{		
					if (sectionDataMap.containsKey(primaryLookupData) && sectionDataMap.containsKey(secondaryLookupData))
					{
						foundData = true;
						break;
					}					
				}
				else
				{
					if (sectionDataMap.containsKey(primaryLookupData))
					{
						foundData = true;
						break;
					}
				}
			}
			
			if (!foundData)
			{						
				addValidJsonTag = false;
			}
		}
		return addValidJsonTag;
	}	
}
