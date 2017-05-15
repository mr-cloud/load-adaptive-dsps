package org.mlgb.dsps.util.dao;

import org.mlgb.dsps.util.Consts;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import uni.akilis.helper.LoggerX;
/**
 * MongoDB connection factory with singleton.
 * @author Leo
 *
 */
public class MyMongoDatabaseFactory {
	private static final String TAG = MyMongoDatabaseFactory.class.getName();
	private static MongoClient client;
	public static MongoDatabase getMongoDatabase(String dbName){
		if(client != null){
			return client.getDatabase(dbName);
		}
		else{
			client = new MongoClient(new MongoClientURI(Consts.MONGO_CLIENT_URI));
			if(client == null){
				LoggerX.error(TAG, "failed to get client!");
				return null;
			}
			return client.getDatabase(dbName);
		}
	}

}
