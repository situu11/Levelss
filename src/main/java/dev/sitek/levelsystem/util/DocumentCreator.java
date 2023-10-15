package dev.sitek.levelsystem.util;

import org.bson.Document;

public class DocumentCreator {

    private DocumentCreator() {
    }

    public static Document newDocument(String uuid, String name) {
        Document document = new Document();
        document.put("uuid", uuid);
        document.put("name", name);
        document.put("level", 1);
        document.put("exp", 0.0);
        document.put("requiredExp", 100.0);
        document.put("bonusExp", 0.0);
        return document;
    }

    public static Document newItemshopDocument(String uuid) {
        Document document = new Document();
        document.put("uuid", uuid);
        document.put("wPLN", 100);
        return document;
    }

    public static Document updateDocument(int level, double exp, double requiredExp, double bonusExp) {
        Document document = new Document();
        document.put("level", level);
        document.put("exp", exp);
        document.put("requiredExp", requiredExp);
        document.put("bonusExp", bonusExp);
        return document;
    }

    public static Document updateItemshopDocument(int wPLN) {
        Document document = new Document();
        document.put("wPLN", wPLN);
        return document;
    }

}
