package me.jwhz.mmorpgcore.profile;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.UpdateOptions;
import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.manager.ManagerObject;
import me.jwhz.mmorpgcore.profile.events.PlayerChangeProfileEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBPlayer extends ManagerObject<UUID> {

    private UUID id;
    private Document data;
    private Profile profile;

    DBPlayer(Player player) {

        this.id = player.getUniqueId();

    }

    public Player getPlayer() {

        return Bukkit.getPlayer(id);

    }

    public void loadData(Document document) {

        this.data = document;

    }

    public Document getData() {

        return data;

    }

    public boolean setCurrentProfile(Profile profile) {

        PlayerChangeProfileEvent event = new PlayerChangeProfileEvent(getPlayer(), this.profile, profile);

        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {

            if (this.profile != null)
                this.profile.unloadProfile();

            this.profile = profile;

            profile.loadProfile();

            data.put("last played", profile.getProfileUUID().toString());

            return true;

        }

        return false;

    }

    public Profile getCurrentProfile() {

        return profile;

    }

    /**
     * @return The last played profile
     */

    public Profile getLastPlayed() {

        return getProfile(UUID.fromString(data.getString("last played")));

    }

    /**
     * @return A boolean if the database has saved a profile that they last played on
     */

    public boolean hasPlayedBefore() {

        return data.containsKey("last played");

    }

    /**
     * Creates a profile with the given name
     *
     * @param name
     */

    public void createProfile(String name) {

        List<Profile> profiles = getProfiles();

        List<Document> documents = new ArrayList<>();

        Document profileData = new Document();
        profileData.put("profile name", name);
        profileData.put("profile uuid", UUID.randomUUID().toString());
        profileData.put("owner", id.toString());

        documents.add(profileData);

        for (Profile profile : profiles)
            documents.add(profile.getData());

        data.put("profiles", documents);

    }

    /**
     * @return A List of all the profiles found in the database
     */

    public List<Profile> getProfiles() {

        List<Profile> list = new ArrayList<>();

        if (data.containsKey("profiles")) {

            List<Document> profiles = (List<Document>) data.get("profiles");

            profiles.forEach(doc -> list.add(new Profile(doc)));

        }

        return list;

    }

    /**
     * @param name
     * @return The targeted profile if it exists.
     */

    public Profile getProfile(String name) {

        return getProfiles().stream().filter(profile -> profile.getProfileName().equalsIgnoreCase(name)).findFirst().orElse(null);

    }

    /**
     * @param name
     * @return Checks to see if name is a valid profile
     */

    public boolean isProfile(String name) {

        return getProfiles().stream().anyMatch(profile -> profile.getProfileName().equalsIgnoreCase(name));

    }

    /**
     * @param uuid
     * @return If a profile with uuid exists
     */

    public Profile getProfile(UUID uuid) {

        return getProfiles().stream().filter(profile -> profile.getProfileUUID().equals(uuid)).findFirst().orElse(null);

    }

    /**
     * @param uuid
     * @return Checks to see if the uuid belongs to a valid profile
     */

    public boolean isProfile(UUID uuid) {

        return getProfiles().stream().anyMatch(profile -> profile.getProfileName().equals(uuid));

    }

    /**
     * Saves all the player's data back to the database.
     */

    public void save() {

        if (core.database.isRegistered(id))
            core.database.collection.replaceOne(new BasicDBObject("uuid", id.toString()), data);
        else
            core.database.collection.insertOne(data);

    }

    public void sendMessage(String message) {

        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));

    }

    @Override
    public UUID getIdentifier() {

        return id;

    }

    /**
     * Static getters
     */

    public static DBPlayer getPlayer(Player player) {

        return getPlayer(player.getUniqueId());

    }

    public static DBPlayer getPlayer(UUID id) {

        return MMORPGCore.getInstance().playerManager.getList().stream().filter(dbPlayer -> dbPlayer.id.equals(id)).findFirst().orElse(null);

    }

}
