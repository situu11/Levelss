package dev.sitek.levelsystem.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    private MongoClient client;
    private MongoCollection<Document> levels;
    private MongoCollection<Document> itemshop;

    public Database() {
        if (!isConnected()) {
            client = new MongoClient();
            MongoDatabase database = client.getDatabase("tryrpg");

            levels = database.getCollection("level");
            itemshop = database.getCollection("itemshop");
        }
    }

    public void close() {
        if (isConnected()) {
            client.close();
        }
    }

    private boolean isConnected() {
        return client != null;
    }

    public MongoCollection<Document> getLevelsCollection() {
        return levels;
    }

    public MongoCollection<Document> getItemshop() {
        return itemshop;
    }

}
