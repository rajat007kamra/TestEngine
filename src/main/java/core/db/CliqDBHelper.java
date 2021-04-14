package core.db;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;

import com.mezocliq.bytes.common.cacher.CacherException;
import com.mezocliq.bytes.commons.cacher.CacheInitializer;
import com.mezocliq.bytes.qrector.conjugates.DatabaseGroup;
import com.mezocliq.bytes.qrector.conjugates.DatabaseRow;
import com.mezocliq.bytes.qrector.conjugates.DatabaseVariable;
import com.mezocliq.bytes.qrector.exception.DatabaseException;
import com.mezocliq.bytes.qrector.exception.QueryException;
import com.mezocliq.bytes.qrector.transaction.SaveException;
import com.mezocliq.bytes.qrector.transaction.SaveResponse;
import com.mezocliq.bytes.thrift.service.TCaption;

import core.cliqdb.model.AttributeData;
import core.cliqdb.model.ProfileData;
import core.cliqdb.model.SectionData;
import core.cliqdb.model.StepData;
import core.cliqdb.model.SummaryData;
import core.cliqdb.model.TestCaseData;

/**
 * @summary This class contains all methods related to accessing TCs in TESTCASES screen
 * @author Manoj.Jain
 */
public class CliqDBHelper 
{
	private static boolean initCacheInitializer = false;
	final static Logger logger = Logger.getLogger(CliqDBHelper.class);
	/** 
	 * @summary This constructor is of singleton type
	 * @param initialize If user intends to initialize this constructor then he has to send value as TRUE. This is helpful in case 
	 * user needs to connect to other database while one is already connected
 	 * @throws CacherException
	 */
	public CliqDBHelper(boolean initialize) throws CacherException {
		if (initialize) {
			if (!initCacheInitializer) {
				CacheInitializer.init();
				initCacheInitializer = true;
			}
		}
	}

	/**
	 * @summary This method is responsible for fetching all steps of a TESTCASE
	 * @param groupIds This specifies group ids of respective test case.
 	 * @return
	 */
	private List<GenericPair> getAllStepNameNumber(List<ByteBuffer> groupIds) {
		List<GenericPair> stepNameNumberpairList = new ArrayList<>();
		for (ByteBuffer gid : groupIds) {
			String id = DataExtractor.convertToString(gid);
			if (id.contains(CommonConstants.STEP)) 
			{				
				String[] idContents = id.split(CommonConstants.UNDERSCORE);
				String stepName = idContents[1];
				String stepNumber = stepName.substring(stepName.lastIndexOf(CommonConstants.SPACE) + 1);				
				GenericPair gp = new GenericPair<String, String>(CommonConstants.STEP + CommonConstants.SPACE + stepNumber, stepNumber);
				stepNameNumberpairList.add(gp);
			}
		}
		return stepNameNumberpairList;
	}
	
	/**
	 * @summary This method is responsible for fetching all sections for all steps of a TESTCASE
	 * @param databaseRow
	 * @return
	 */
	private Map<GenericPair, List<ByteBuffer>> getAllSections(DatabaseRow databaseRow) 
	{
		Map<GenericPair, List<ByteBuffer>> stepMap = new HashedMap<GenericPair, List<ByteBuffer>>();
		List<ByteBuffer> groupIds = new ArrayList<ByteBuffer>();
		Map<ByteBuffer, Map<Long, DatabaseGroup>> groups = databaseRow.getGroups();
		Set<ByteBuffer> keySet = groups.keySet();
		for (ByteBuffer groupKey : keySet) {
			groupIds.add(groupKey);
		}
		
		// Get the stepName-StepSequenceNumber as a pair
		List<GenericPair> allSteps = getAllStepNameNumber(groupIds);
		for (GenericPair stepPair : allSteps) 
		{
			String stepName = stepPair.getDataDescriptor().toString();
			List<ByteBuffer> result = groupIds.stream()
					.filter(line -> DataExtractor.convertToString(line).contains(stepName))
					.collect(Collectors.toList());
			stepMap.put(stepPair, result);
		}

		return stepMap;
	}

	/**
	 * @summary This method is responsible for fetching all variables related to a database group 
	 * @param databaseGroup
	 * @return
	 */
	private Map<String, String> getVariableList(DatabaseGroup databaseGroup) {
		Map<String, String> variableMap = new HashMap<String, String>();
		Collection<DatabaseVariable> variables = databaseGroup.getVariables();
		for (DatabaseVariable databaseVariable : variables) {
			ByteBuffer variableId = databaseVariable.getVariableId();
			String variable = DataExtractor.convertToString(variableId);
			String[] varComponents = variable.split("_");
			String varId = varComponents[varComponents.length - 1];
			String variableValue = databaseVariable.getValue().toString();
			variableMap.put(varId, variableValue);
		}

		return variableMap;
	}

	/**
	 * @summary This method is responsible for fetching all variables related to multiple database groups
	 * @param groupMap
	 * @return
	 */
	private List<Map<String, String>> getVariableList(Map<Long, DatabaseGroup> groupMap) {
		List<Map<String, String>> varList = new ArrayList<Map<String, String>>();
		if (groupMap != null) {
			Set<Long> keySet = groupMap.keySet();
			for (Long key : keySet) {
				Map<String, String> variableMap = new HashMap<String, String>();

				DatabaseGroup databaseGroup = groupMap.get(key);
				Collection<DatabaseVariable> variables = databaseGroup.getVariables();
				for (DatabaseVariable databaseVariable : variables) {
					ByteBuffer variableId = databaseVariable.getVariableId();
					String variable = DataExtractor.convertToString(variableId);
					String[] varComponents = variable.split("_");
					String varId = varComponents[varComponents.length - 1];
					String variableValue = databaseVariable.getValue().toString();
					variableMap.put(varId, variableValue);
				}
				varList.add(variableMap);
			}
		}

		return varList;
	}

	/**
	 * @summary This method is responsible for fetching profile data related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private ProfileData getProfileData(DatabaseRow databaseRow) {
		ProfileData profileData = new ProfileData();
		List<Map<String, String>> categoryMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.CATEGORY_KEY)));

		List<Map<String, String>> batteryMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.BATTERY_KEY)));
		List<Map<String, String>> conditionsMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.CONDITION_KEY)));
		List<Map<String, String>> modeMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.MODE_KEY)));
		if (categoryMap != null && batteryMap != null) {

			if (!batteryMap.isEmpty()) {
				profileData.setSubcategory(batteryMap.get(0).get("I"));
				profileData.setCategory(categoryMap.get(0).get("I"));
				profileData.setConditions(conditionsMap.get(0).get("I"));
				profileData.setMode(modeMap.get(0).get("I"));
			}
		}
		return profileData;
	}

	/**
	 * @subject This method is responsible for fetching summary related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private SummaryData getSummaryData(DatabaseRow databaseRow) {
		SummaryData summaryData = new SummaryData();
		List<Map<String, String>> descriptionMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.DESCRIPTION_KEY)));
		if (!descriptionMap.isEmpty()) {
			summaryData.setDescription(descriptionMap.get(0).get("I"));
		}
		return summaryData;
	}

	/**
	 * @subject This method is responsible for fetching attribute data related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private AttributeData getAttributeData(DatabaseRow databaseRow) {
		AttributeData attributeData = new AttributeData();

		List<Map<String, String>> directionMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.DIRECTION_KEY)));
		List<Map<String, String>> formateMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.FORMATE_KEY)));

		List<Map<String, String>> captionMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.API_CAPTION)));

		List<Map<String, String>> configurationMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.CONFIGURATION)));

		List<Map<String, String>> useMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.USEAGE)));

		List<Map<String, String>> methodMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.METHOD)));

		List<Map<String, String>> rhythmMap = getVariableList(
				databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.RHYTHM)));

		if (!directionMap.isEmpty()) {
			attributeData.setDirection(directionMap.get(0).get("I"));

		}
		if (!formateMap.isEmpty()) {
			attributeData.setDelivery_format(formateMap.get(0).get("I"));
		}

		if (!captionMap.isEmpty()) {
			attributeData.setApiCaption(captionMap.get(0).get("DB-ID"));
		}
		if (!configurationMap.isEmpty()) {
			attributeData.setConfiguration(configurationMap.get(0).get("I"));
		}
		if (!useMap.isEmpty()) {
			attributeData.setUsage(useMap.get(0).get("I"));
		}
		if (!methodMap.isEmpty()) {
			attributeData.setMethod(methodMap.get(0).get("I"));
		}
		if (!rhythmMap.isEmpty()) {
			attributeData.setRhythm(rhythmMap.get(0).get("I"));
		}

		return attributeData;
	}

	/**
	 * @summary This method is responsible for fetching CAPTION related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	public Map<String, String> getCaption(DatabaseRow databaseRow) {
		return getVariableList(databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.TRACKING_RECORD))).get(0);
	}

	/**
	 * @summary This method is responsible for fetching STATE related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private Map<String, String> getState(DatabaseRow databaseRow) {
		return getVariableList(databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.PHASING_STATE))).get(0);
	}

	/**
	 * @summary This method is responsible for fetching ACTIVATION DATE related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private Map<String, String> getActivationDate(DatabaseRow databaseRow) {
		return getVariableList(databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.WORKFLOW_TIMING))).get(0);
	}

	/**
	 * @summary This method is responsible for fetching Z-ID related to a TESTCASE (aka database row)
	 * @param databaseRow
	 * @return
	 */
	private Map<String, String> getEnterpriseID(DatabaseRow databaseRow) {
		return getVariableList(databaseRow.getGroups(DataExtractor.convertToByteBuffer(DataBaseConstants.ENTERPRISE_ID))).get(0);
	}

	/**
	 * @summary - This method populates POJO by fetching values using various methods for a TESTCASE.
	 *  
	 * @param databaseRow
	 * @return
	 */
	public TestCaseData constructTestCaseData(DatabaseRow databaseRow) 
	{
		TestCaseData testCaseData = new TestCaseData();
		List<StepData> stepList = new ArrayList<StepData>();
		if (getProfileData(databaseRow) == null) {
			return null;
		}
		Map<GenericPair, List<ByteBuffer>> steps = getAllSections(databaseRow);
		Set<GenericPair> stepKeys = steps.keySet();
		for (GenericPair keyPair : stepKeys) {
			StepData stepData = new StepData();
			stepData.setStepName(keyPair.getDataDescriptor().toString());
			stepData.setStepSequenceNumber(Integer.valueOf(keyPair.getDataValue().toString()));
			List<SectionData> sectionList = new ArrayList<SectionData>();
			List<ByteBuffer> groupsList = steps.get(keyPair);
			for (ByteBuffer grpKey : groupsList) {
				SectionData sectionData = new SectionData();
				String groupName = DataExtractor.convertToString(grpKey);
				String[] grpComponents = groupName.split(CommonConstants.UNDERSCORE);
				String gname = grpComponents[grpComponents.length - 1];

				if (gname.equalsIgnoreCase("PARAMETERS")) {
					Map<Long, DatabaseGroup> groupMap = databaseRow.getGroups(grpKey);
					Map<String, String> paramsMap = getVariableList(groupMap).get(0);
					stepData.setUser(paramsMap.get("USER"));
					stepData.setRecordType(paramsMap.get("RECORD TYPE"));
					stepData.setCount(paramsMap.get("RECORD COUNT"));
					stepData.setStepType(paramsMap.get("STEP TYPE"));
				}

				sectionData.setName(gname);
				
				List<Map<String, String>> variableList = new ArrayList<Map<String, String>>();
				Map<Long, DatabaseGroup> groupMap = databaseRow.getGroups(grpKey);
				variableList.addAll(getVariableList(groupMap));
				sectionData.setSectionContext(variableList);
				sectionList.add(sectionData);
			}
			stepData.setStepSections(sectionList);
			stepList.add(stepData);
		}
		testCaseData.setSteps(stepList);
		testCaseData.setAttributeData(getAttributeData(databaseRow));
		testCaseData.setProfileData(getProfileData(databaseRow));
		testCaseData.setSummaryData(getSummaryData(databaseRow));
		testCaseData.setRowKey(databaseRow.getRowkey().toString());
		testCaseData.setCaption(getCaption(databaseRow).get("CAPTION"));
		testCaseData.setState(getState(databaseRow).get("STATE"));
//		testCaseData.setActivationDate(getActivationDate(databaseRow).get("ACTIVATED"));
		testCaseData.setEnterpriseId(getEnterpriseID(databaseRow).get("#"));

		return testCaseData;
	}

	/**
	 * @summary This method connects to database and populates data (POJO) for all TESTCASES
	 * @return
	 * @throws Exception
	 */
	public List<TestCaseData> getTestCaseData() throws Exception {
		List<TestCaseData> testCases = new ArrayList<TestCaseData>();
		List<DatabaseRow> rows = DataOrchestrator.getRows(DataBaseConstants.TABLE_NAME);
		logger.info("SYNCJOB: rows retrieved from db " + rows.size());
		for (DatabaseRow databaseRow : rows) 
		{
			TestCaseData testCase = constructTestCaseData(databaseRow);
			if (testCase != null) 
			{			
				testCases.add(testCase);
			}
		}
		return testCases;
	}

	/**
	 * @summary - This method fetches CAPTION of a variable based on their UUID.
	 * This is different than getCaption(DatabaseRow) as that fetches caption of a TESTCASE.
	 * @param keys
	 * @return
	 * @throws DatabaseException
	 */
	public Map<String, String> getCaption(Set<UUID> keys) throws DatabaseException {
		Map<String, String> captionMap = new HashMap<String, String>();
		Map<UUID, TCaption> caption = DataOrchestrator.getCaption(keys);
		Set<UUID> keySet = caption.keySet();
		for (UUID uuid : keySet) {
			TCaption tCaption = caption.get(uuid);

			byte[] captionFormatted = tCaption.getCaption();
			ByteBuffer key = ByteBuffer.wrap(captionFormatted);
			String captionValue = DataExtractor.convertToString(key);
			captionMap.put(uuid.toString(), captionValue);
		}
		return captionMap;

	}

	/**
	 * @subject This method is responsible for fetching SCREEN data for a TESTCASE (From SCREEN table)
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, String>> getScreenData() throws Exception {

		Map<String, Map<String, String>> screepProcessMap = new HashMap<String, Map<String, String>>();

		List<DatabaseRow> rows = DataOrchestrator.getRows(DataBaseConstants.SCREEN_TABLE_NAME);
		for (DatabaseRow databaseRow : rows) {
			Map<String, String> varMap = new HashMap<String, String>();

			Map<Long, DatabaseGroup> specificityGroup = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("TAGS_INSTRUCTION_SPECIFICITY"));
			Map<Long, DatabaseGroup> scopeGroup = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("RELATIONSHIPS_COVERAGE_SCOPE"));

			if (scopeGroup != null && specificityGroup != null) {
				Set<Long> specificityKeySet = specificityGroup.keySet();
				for (Long key : specificityKeySet) {
					DatabaseGroup databaseGroup = specificityGroup.get(key);
					varMap.putAll(getVariableList(databaseGroup));

				}
				Set<Long> scopeKeySet = scopeGroup.keySet();
				for (Long key : scopeKeySet) {
					DatabaseGroup databaseGroup = scopeGroup.get(key);
					varMap.putAll(getVariableList(databaseGroup));
				}

				screepProcessMap.put(databaseRow.getRowkey().toString(), varMap);
			}
		}

		return screepProcessMap;
	}

	/**
	 * @summary This method is responsible for fetching MZONE data for a TESTCASE (From MZONE table).
	 * This is to find tab name from process name as there were issues due to singular/plural names
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getMzoneData() throws Exception {
		Map<String, String> IdNameMap = new HashMap<String, String>();
		List<DatabaseRow> systemTenentRowsRows = DataOrchestrator
				.getSystemTenentRowsRows(DataBaseConstants.MZONE_TABLE_NAME);
		for (DatabaseRow databaseRow : systemTenentRowsRows) {
			String dbId = null;
			String fullName = null;
			Map<Long, DatabaseGroup> nameGroups = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("DIRECTORY_NAME_TECHNICAL"));
			Map<Long, DatabaseGroup> idGroup = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("RELATIONSHIPS_AFFILIATIONS_EZONE"));
			if (idGroup != null && nameGroups != null) {
				Set<Long> nameKeySet = nameGroups.keySet();
				for (Long key : nameKeySet) {
					fullName = getVariableList(nameGroups).get(0).get("FULL");

				}
				Set<Long> idKeySet = idGroup.keySet();
				for (Long key : idKeySet) {
					dbId = getVariableList(idGroup).get(0).get("DB-ID");
				}
				IdNameMap.put(dbId, fullName);
			}
		}
		return IdNameMap;
	}

	/**
	 * @summary  This method is responsible for fetching SIMPLE NAME from DICTIONARY table against a DB-ID 
	 * @deprecated
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllSimpleName() throws Exception {
		Map<String, String> nameMap = new HashMap<String, String>();
		List<DatabaseRow> rows = DataOrchestrator.getRows(DataBaseConstants.DICTIONARY_TABLE_NAME);
		for (DatabaseRow databaseRow : rows) {
			Map<Long, DatabaseGroup> simpleNameGroup = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("DIRECTORY_NAME_TECHNICAL"));
			Map<Long, DatabaseGroup> idGroup = databaseRow
					.getGroups(DataExtractor.convertToByteBuffer("REFERENCING_TRACKING_RECORD"));
			nameMap.put(getVariableList(idGroup).get(0).get("DB-ID"),
					getVariableList(simpleNameGroup).get(0).get("SIMPLE"));

		}
		return nameMap;

	}

	/**
	 * @summary Fetching database rows for a table
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<DatabaseRow> getDBRows(String tableName) throws Exception {

		return DataOrchestrator.getRows(tableName);
	}

	/**
	 * @summary Marks a row for REVISION and it was required for DB clean-up
	 * @deprecated This will not be used as approach for cleaning up DB post run has changed.
	 * @param databaseRow
	 * @return
	 * @throws QueryException
	 * @throws SaveException
	 */
	private SaveResponse markRevise(DatabaseRow databaseRow) throws QueryException, SaveException {
		String startAction = "INITIATE";
		String endAction = "INITIATE";
		return DataOrchestrator.performUpdate(databaseRow, startAction, endAction);

	}

	/**
	 * @summary Marks a row for WITHDRAW and it was required for DB clean-up
	 * @deprecated This will not be used as approach for cleaning up DB post run has changed.
	 * @param databaseRow
	 * @return
	 * @throws QueryException
	 * @throws SaveException
	 */
	private SaveResponse markWithdraw(DatabaseRow databaseRow) throws QueryException, SaveException {
		String startAction = "DRAFT";
		String endAction = "COMPOSE";
		return DataOrchestrator.performUpdate(databaseRow, startAction, endAction);

	}

	/**
	 * @summary Marks a row for ACTIVE and it was required for DB clean-up
	 * @deprecated This will not be used as approach for cleaning up DB post run has changed.
	 * @param databaseRow
	 * @return
	 * @throws QueryException
	 * @throws SaveException
	 */
	public SaveResponse markActive(DatabaseRow databaseRow) throws QueryException, SaveException {
		String startAction = "DRAFT";
		String endAction = "COMPLETE";
		return DataOrchestrator.performUpdate(databaseRow, startAction, endAction);
	}
}
