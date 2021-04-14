package core.db;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

/**
 * @summary This class is responsible for interacting with MongoDB which is being used for storing user credentials
 * @author Surendra.Shekhawat
 * TODO Rename this class to MongoDBOrchestrator
 */
public class UserDBUpdate {

	private String _username = "username";
	private String _password = "password";
	private String _userId = "userId";
	private String _subTenant = "subtenant";
	private String lastUpdated = "lastUpdated";
	private String tempPassword = "tempPassword";
	private String connectUrl;
	private static MongoClient mongoClient;
	final static Logger logger = Logger.getLogger(UserDBUpdate.class);
	/**
	 * @summary Singleton constructor for MongoDB and to be instantiated only if there is no instance exists
	 * @param connectUrl
	 */
	public UserDBUpdate(String connectUrl) {
		this.connectUrl = connectUrl;
		if (mongoClient == null) {
			mongoClient = new MongoClient(new MongoClientURI(connectUrl));
		}
	}

	/**
	 * @summary Get user info for user id being provided. 
	 * @param userId
	 * @return
	 */
	private Document getUserInfo(String userId) {
		try {
			MongoDatabase mongoDatabase = mongoClient.getDatabase("userinfo");
			MongoCollection<Document> collection = mongoDatabase.getCollection("users");
			FindIterable<Document> find = collection.find();
			for (Document document : find) {
				Object object = document.get(_userId);
				if (object.toString().equalsIgnoreCase(userId)) {
					return document;
				}
			}
		} finally {
			mongoClient.close();
			mongoClient = null;
		}
		return null;
	}

	/**
	 * @summary Updates user info for user id being provided. 
	 * @param userId
	 * @return
	 */
	private UpdateResult updateUserInfo(String userId, String key, String value) {
		try {
			MongoDatabase mongoDatabase = mongoClient.getDatabase("userinfo");
			MongoCollection<Document> collection = mongoDatabase.getCollection("users");
			Bson filter = eq("userId", userId);
			Bson updateOperation = set(key, value);
			UpdateResult updateResult = collection.updateOne(filter, updateOperation);
			logger.info(updateResult);
			return updateResult;
		} finally {
			mongoClient.close();
			mongoClient = null;
		}
	}

	/**
	 * @summary Public method for getUserInfo 
	 * @param userId
	 * @return
	 */
	public String getUserDetails(String userId) {
		Document userInfo = getUserInfo(userId);
		return userInfo.toJson();
	}

	/**
	 * @summary Public method for getPassword
	 * @param userId
	 * @return
	 */
	public String getPassword(String userId) {
		return getUserInfo(userId).get(_password).toString();
	}

	/**
	 * @summary Public method to update password 
	 * @param userId
	 * @param newPassword
	 */
	public void updatePassword(String userId, String newPassword) {
		updateUserInfo(userId, _password, newPassword);

	}

	/**
	 * @summary Public method for updating subtenant 
	 * @param userId
	 * @param newSubTenant
	 * @return
	 */
	public String updateSubTenant(String userId, String newSubTenant) {
		Document userInfo = getUserInfo(userId);

		return userInfo.put(_subTenant, newSubTenant).toString();
	}

	/**
	 * @summary Public method for fetching subtenant for user id provided
	 * @param userId
	 * @return
	 */
	public String getSubtenant(String userId) {
		Document userInfo = getUserInfo(userId);
		return userInfo.getString(_subTenant).toString();
	}
}