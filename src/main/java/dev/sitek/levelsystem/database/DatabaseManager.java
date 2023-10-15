package dev.sitek.levelsystem.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.instance.Level;
import dev.sitek.levelsystem.util.DocumentCreator;
import org.bson.Document;

import java.util.UUID;

public class DatabaseManager {

    private final LevelSystem plugin;
    private final MongoCollection<Document> levels;
    private final MongoCollection<Document> itemshop;

    /*
    * Database initializing
     */

    static {
        LevelSystem.database = new Database();
    }

    public DatabaseManager(LevelSystem plugin, MongoCollection<Document> levels, MongoCollection<Document> itemshop) {
        this.plugin = plugin;
        this.levels = levels;
        this.itemshop = itemshop;
    }

    /*
     * Database inserting new player * logic
     */

    public void createUser(UUID uuid, String playerName) {
        if (!isExits(uuid)) {
            levels.insertOne(DocumentCreator.newDocument(uuid.toString(), playerName));
            itemshop.insertOne(DocumentCreator.newItemshopDocument(uuid.toString()));
        }
    }

    private boolean isExits(UUID uuid) {
        return levels.countDocuments(Filters.eq("uuid", uuid.toString())) > 0;
    }

    /*
     * Database saving logic
     */

    public void saveAllPlayersData() {
        for (UUID uuid : plugin.getLevelManager().getUsersMap().keySet()) {
            savePlayerData(uuid);
        }
    }

    public void savePlayerData(UUID uuid) {
        Level level = plugin.getLevelManager().getLevel(uuid);
        if (level != null) {
            updatePlayerData(uuid, level);
        }
    }

    private void updatePlayerData(UUID uuid, Level level) {
        Document query = new Document("uuid", uuid.toString());
        Document newDocument = DocumentCreator.updateDocument(level.getLevel(), level.getExp(), level.getRequiredExp(), level.getBonusExp());
        Document updated = new Document("$set", newDocument);
        levels.updateMany(query, updated);
    }

    public void updateItemshopData(UUID uuid, int load) {
        Document query = new Document("uuid", uuid.toString());
        Document newDocument = DocumentCreator.updateItemshopDocument(getwPLN(uuid) - load);
        Document updated = new Document("$set", newDocument);
        itemshop.updateOne(query, updated);
    }

    /*
     * Database getters
     */

    private Document getDocument(UUID uuid) {
        return levels.find(Filters.eq("uuid", uuid.toString())).first();
    }


    private Document getDocument(String playerName) {
        return levels.find(Filters.eq("name", playerName)).first();
    }

    private Document getItemshopDocument(UUID uuid) {
        return itemshop.find(Filters.eq("uuid", uuid.toString())).first();
    }


    public UUID getPlayerUUID(String playerName) {
        Document document = getDocument(playerName);
        return document != null ? UUID.fromString(document.getString("uuid")) : null;
    }

    public String getPlayerName(UUID uuid) {
        Document document = getDocument(uuid);
        return document != null ? document.getString("name") : null;
    }

    public int getLevel(UUID uuid) {
        Document document = getDocument(uuid);
        return document != null ? document.getInteger("level") : -1;
    }

    public int getLevel(String playerName) {
        Document document = getDocument(playerName);
        return document != null ? document.getInteger("level") : -1;
    }

    public double getExp(UUID uuid) {
        Document document = getDocument(uuid);
        return document != null ? document.getDouble("exp") : -1.0;
    }

    public double getRequiredExp(UUID uuid) {
        Document document = getDocument(uuid);
        return document != null ? document.getDouble("requiredExp") : -1.0;
    }

    public double getBonusExp(UUID uuid) {
        Document document = getDocument(uuid);
        return document != null ? document.getDouble("bonusExp") : -1.0;
    }

    public MongoCollection<Document> getLevels() {
        return levels;
    }

    public int getwPLN(UUID uuid) {
        Document document = getItemshopDocument(uuid);
        return document != null ? document.getInteger("wPLN") : -1;
    }

    public MongoCollection<Document> getItemshop() {
        return itemshop;
    }
}
