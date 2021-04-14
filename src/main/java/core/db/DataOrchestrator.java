package core.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.mezocliq.bytes.qrector.conjugates.DatabaseRow;
import com.mezocliq.bytes.qrector.exception.DatabaseException;
import com.mezocliq.bytes.qrector.exception.QueryException;
import com.mezocliq.bytes.qrector.query.Query;
import com.mezocliq.bytes.qrector.query.Query.QueryBuilder;
import com.mezocliq.bytes.qrector.transaction.SaveBuilder;
import com.mezocliq.bytes.qrector.transaction.SaveException;
import com.mezocliq.bytes.qrector.transaction.SaveExecutor;
import com.mezocliq.bytes.qrector.transaction.SaveResponse;
import com.mezocliq.bytes.qrector.query.QueryExecutor;
import com.mezocliq.bytes.qrector.query.RowSet;
import com.mezocliq.bytes.thrift.service.TCaption;
import com.mezocliq.bytes.thrift.service.TUserContext;

/**
 * @summary This is the class which interacts with CLIQ database. 
 * @author Manoj.Jain
 */
public class DataOrchestrator {
	final static Logger logger = Logger.getLogger(DataOrchestrator.class);
	/**
	 * @summary This method is responsible for fetching all rows for specified table
	 * @param tableName
	 * @return
	 * @throws Exception
	 * TODO Limiter with battery [Consult with Sugath]
	 */
	public static List<DatabaseRow> getRows(String tableName) throws Exception {

		QueryExecutor queryExecutor = null;
		List<DatabaseRow> rowList = new ArrayList<>();
		try {
			QueryBuilder queryBuilder = new Query.QueryBuilder(createUserContext()).from(tableName);
			queryBuilder.limit(1000);
			queryBuilder.fetch(1000);
//			queryBuilder.valid();
			Query query = queryBuilder.build();
			queryExecutor = new QueryExecutor();
			RowSet resultSet = queryExecutor.executeRead(query);
			logger.info("done with query");
			while (resultSet.next()) {
				DatabaseRow databaseRow = resultSet.getDatabaseRow();
				resetStages(databaseRow);
				rowList.add(databaseRow);
			}

		} catch (QueryException e) {
			e.printStackTrace();
		} finally {
			if (queryExecutor != null) {
				queryExecutor.close();
			}
		}
		return rowList;
	}

	/**
	 * @summary This method is responsible to fetch all rows from SYSTEM tenant. Being used by getMzoneData method.
	 * In
	 * @param tableName
	 * @return
	 * @throws Exception
	 * TODO To check w/ Sugath about why SCREEN table is not using this method?
	 */
	public static List<DatabaseRow> getSystemTenentRowsRows(String tableName) throws Exception {
		QueryExecutor queryExecutor = null;
		List<DatabaseRow> rowList = new ArrayList<>();
		try {
			QueryBuilder queryBuilder = new Query.QueryBuilder(createSystemUserContext()).from(tableName);
			queryBuilder.limit(1000);
			queryBuilder.fetch(1000);
			queryBuilder.valid();
			Query query = queryBuilder.build();
			queryExecutor = new QueryExecutor();
			RowSet resultSet = queryExecutor.executeRead(query);
			while (resultSet.next()) {
				DatabaseRow databaseRow = resultSet.getDatabaseRow();
				resetStages(databaseRow);
				rowList.add(databaseRow);
			}

		} catch (QueryException e) {
			e.printStackTrace();
		} finally {
			if (queryExecutor != null) {
				queryExecutor.close();
			}
		}
		return rowList;
	}

	/**
	 * @summary Prepares user context for TEST tenant
	 * @return
	 */
	public static com.mezocliq.bytes.qrector.query.UserContext createUserContext() {
		com.mezocliq.bytes.qrector.query.UserContext bytesUser = new com.mezocliq.bytes.qrector.query.UserContext(
				DataBaseConstants.TEST_TENANTID);
		bytesUser.setLoggedInSubTenant(DataBaseConstants.SYSTEM_TENANTDBID);
		bytesUser.setUserUUID(DataBaseConstants.USER_UUID);
		bytesUser.setSector(DataBaseConstants.SECTOR_NAME);

		return bytesUser;
	}

	/**
	 * @summary Prepares user context for SYSTEM tenant
	 * @return
	 */
	public static com.mezocliq.bytes.qrector.query.UserContext createSystemUserContext() {
		com.mezocliq.bytes.qrector.query.UserContext bytesUser = new com.mezocliq.bytes.qrector.query.UserContext(
				DataBaseConstants.SYSTEM_TENANTDBID);
		bytesUser.setLoggedInSubTenant(DataBaseConstants.SYSTEM_TENANTDBID);
		bytesUser.setUserUUID(DataBaseConstants.USER_UUID);
		bytesUser.setSector(DataBaseConstants.SECTOR_NAME);

		return bytesUser;
	}

	/**
	 * @summary Reset stage according to pagination while reading rows from database
	 * @return
	 */
	private static void resetStages(DatabaseRow databaseRow) {
		databaseRow.setStartStage("");
		databaseRow.setEndStage("");
		databaseRow.setSupportingData(null);
	}

	/**
	 * @summary Creates user context to read CAPTION table which is under SYSTEM tenant
	 * @return
	 */
	public static TUserContext createTUserContext() {
		TUserContext bytesUser = new TUserContext(DataBaseConstants.TEST_TENANTID, DataBaseConstants.USER_UUID);
		bytesUser.setLoggedInSubTenant(DataBaseConstants.SYSTEM_TENANTDBID);
		bytesUser.setSector(DataBaseConstants.SECTOR_NAME);

		return bytesUser;
	}

	/**
	 * @summary Get all CAPTIONS against set of UUIDs from CAPTION table which is under SYSTEM tenant
	 * @return
	 */
	public static Map<UUID, TCaption> getCaption(Set<UUID> keys) throws DatabaseException {
		QueryExecutor queryExecutor = new QueryExecutor();
		return queryExecutor.getCaption(createTUserContext(), keys);
	}

	/**
	 * @summary Generic method to set STATE of DB row based on start and end state provided.
	 * This method is called by  markActive, markRevise & markWithdraw methods
	 * @param databaseRow
	 * @param startAction
	 * @param endAction
	 * @return
	 * @throws QueryException
	 * @throws SaveException
	 */
	public static SaveResponse performUpdate(DatabaseRow databaseRow, String startAction, String endAction)
			throws QueryException, SaveException {
		databaseRow.setStartStage(startAction);
		databaseRow.setEndStage(endAction);
		SaveBuilder saveBuilder = new SaveBuilder(createSystemUserContext());
		saveBuilder.add(databaseRow);
		SaveExecutor saveExecutor = new SaveExecutor();
		return saveExecutor.commit(saveBuilder);

	}
}
