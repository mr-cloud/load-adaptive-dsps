package org.mlgb.dsps.util.dao;

import org.bson.Document;
import org.mlgb.dsps.util.Consts;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
/**
 * metrics DAO with MongoDB
 * @author Leo
 *
 */
public class MetricsDAO {
    private MongoCollection<Document> collection;

    public MetricsDAO(final MongoDatabase stormDatabase) {
        this.collection = stormDatabase.getCollection(Consts.METRICS_COLLECTION);
    }

    public void saveMetrics(Document doc) {
        this.collection.insertOne(doc);
    }
    
    
}
