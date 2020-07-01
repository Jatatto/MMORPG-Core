package me.jwhz.mmorpgcore.database.mysql;

import com.mongodb.*;
import me.jwhz.mmorpgcore.config.ConfigFile;
import me.jwhz.mmorpgcore.database.IDatabase;
import org.bukkit.Bukkit;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class MongoDatabase extends ConfigFile implements IDatabase {

    private MongoClient mongoClient;
    public DB database;
    public DBCollection collection;

    public MongoDatabase() {

        super("mongodb");

        try {

            ServerAddress address = new ServerAddress(getYamlConfiguration().getString("ip"), getYamlConfiguration().getInt("port"));

            MongoCredential credential = MongoCredential.createCredential(
                    getYamlConfiguration().getString("username"),
                    getYamlConfiguration().getString("database"),
                    getYamlConfiguration().getString("password").toCharArray()
            );

            List<MongoCredential> credentials = new ArrayList<>();

            credentials.add(credential);

            mongoClient = new MongoClient(address, credentials);

        } catch (UnknownHostException e) {

            Bukkit.getLogger().log(Level.SEVERE, "Unable to find database, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(core);

            e.printStackTrace();
            return;
        }

        database = mongoClient.getDB(getYamlConfiguration().getString("database"));

        collection = database.getCollection("players");

    }

    @Override
    public Object retrieve(UUID id, String key) {

        if (isRegistered(id))
            return getPlayer(id).get(key);

        return null;

    }

    @Override
    public void store(UUID id, String key, Object value) {

        if (isRegistered(id)) {

            DBObject replace = new BasicDBObject("$set", getPlayer(id));
            replace.put("$set", new BasicDBObject(key, value));

            collection.update(getPlayer(id), replace);

        } else {

            DBObject playerProfile = new BasicDBObject("uuid", id.toString());
            playerProfile.put(key, value);

            collection.insert(playerProfile);

        }

    }

    @Override
    public boolean isSet(UUID id, String key) {

        return false;

    }

    @Override
    public boolean isRegistered(UUID id) {

        return collection.findOne(new BasicDBObject("uuid", id.toString())) != null;

    }

    public DBObject getPlayer(UUID id) {

        if (!isRegistered(id))
            return null;

        return collection.findOne(new BasicDBObject("uuid", id.toString()));

    }

}
