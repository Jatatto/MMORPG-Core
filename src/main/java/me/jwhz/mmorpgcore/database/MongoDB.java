package me.jwhz.mmorpgcore.database;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;

import me.jwhz.mmorpgcore.config.ConfigFile;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.UUID;

public class MongoDB extends ConfigFile {

    public MongoClient mongoClient;
    public MongoDatabase database;
    public MongoCollection<Document> collection;

    public MongoDB() {

        super("mongodb");

        mongoClient = MongoClients.create(getYamlConfiguration().getString("uri"));
        database = mongoClient.getDatabase(getYamlConfiguration().getString("database"));
        collection = database.getCollection("players");

    }

    public boolean isRegistered(UUID id) {

        MongoCursor<Document> results = collection.find(new BasicDBObject("uuid", id.toString())).cursor();

        while (results.hasNext())
            if (results.next() != null)
                return true;

        return false;

    }

    public Document getPlayer(UUID id) {

        if (!isRegistered(id))
            return null;

        MongoCursor<Document> results = collection.find(new BasicDBObject("uuid", id.toString())).cursor();

        while (results.hasNext()) {

            Document doc;
            if ((doc = results.next()) != null)
                return doc;

        }

        return null;

    }

}
