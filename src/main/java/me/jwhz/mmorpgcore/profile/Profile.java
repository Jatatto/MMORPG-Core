package me.jwhz.mmorpgcore.profile;

import me.jwhz.mmorpgcore.profile.profiledata.PlayerStats;
import me.jwhz.mmorpgcore.profile.profiledata.ProfileData;
import me.jwhz.mmorpgcore.profile.profiledata.ProfileSettings;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {

    private Document data;
    private List<ProfileData> profileDatas;
    private ProfileSettings profileSettings;
    private PlayerStats playerStats;

    public Profile(Document data) {

        this.data = data;

        this.profileDatas = new ArrayList<>();

        profileDatas.add((profileSettings = new ProfileSettings(this, data.containsKey("profile settings") ? (Document) data.get("profile settings") : new Document())));
        profileDatas.add((playerStats = new PlayerStats(this, data.containsKey("player stats") ? (Document) data.get("player stats") : new Document())));

    }

    public String getProfileName() {

        return String.valueOf(data.get("profile name"));

    }

    public UUID getProfileUUID() {

        return UUID.fromString(data.getString("profile uuid"));

    }

    public UUID getOwner() {

        return UUID.fromString(data.getString("owner"));

    }

    public ProfileSettings getProfileSettings() {

        return profileSettings;

    }

    public PlayerStats getPlayerStats() {

        return playerStats;

    }

    public void unloadProfile() {

        Player player = Bukkit.getPlayer(getOwner());

        save();

        profileDatas.forEach(profileData -> profileData.unload(player));

    }

    public void loadProfile() {

        Player player = Bukkit.getPlayer(getOwner());

        profileDatas.forEach(profileData -> profileData.load(player));

    }

    public void save() {

        Player player = Bukkit.getPlayer(getOwner());

        profileDatas.forEach(profileData -> profileData.save(player));

    }

    public Document getData() {

        return data;

    }

}

