package me.jwhz.mmorpgcore.profile;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.profile.profiledata.PlayerStats;
import me.jwhz.mmorpgcore.profile.profiledata.ProfileSettings;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Profile {

    private MMORPGCore core = MMORPGCore.getInstance();

    private Document data;
    private ProfileSettings profileSettings;
    private PlayerStats playerStats;
    private List<Passive> passives;

    public Profile(Document data) {

        this.data = data;

        profileSettings = new ProfileSettings(this, data.containsKey("profile settings") ? (Document) data.get("profile settings") : new Document());
        playerStats = new PlayerStats(this, data.containsKey("player stats") ? (Document) data.get("player stats") : new Document());

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

    public List<Passive> getPassives() {

        return passives;

    }

    public RPGClass getRPGClass() {

        if (data.containsKey("rpgclass")) {

            RPGClass rpgClass = core.rpgClassManager.getRPGClass(data.getString("rpgclass"));

            if (rpgClass != null)
                passives = rpgClass.getPassives();

            return rpgClass;

        }

        return null;

    }

    public void setRPGClass(RPGClass rpgClass) {

        data.put("rpgclass", rpgClass.getClassName());

        this.passives = rpgClass.getPassives();

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

        profileSettings.unload(player);
        playerStats.unload(player);

    }

    public void loadProfile() {

        Player player = Bukkit.getPlayer(getOwner());

        profileSettings.load(player);
        playerStats.load(player);

        if(getRPGClass() == null){



        }

    }

    public void save() {

        Player player = Bukkit.getPlayer(getOwner());

        profileSettings.save(player);
        playerStats.save(player);

    }

    public Document getData() {

        return data;

    }

}

