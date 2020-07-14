package me.jwhz.mmorpgcore.profile;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.gui.guis.SelectClassGUI;
import me.jwhz.mmorpgcore.profile.profiledata.PlayerStats;
import me.jwhz.mmorpgcore.profile.profiledata.ProfileSettings;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

        getRPGClass();

        if (passives == null)
            passives = new ArrayList<>();

        for (Passive passive : passives)
            passive.setProfile(this);

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

        List<Passive> passiveList = new ArrayList<>();

        passiveList.addAll(passives);
        passiveList.addAll(core.totemManager.getAdditionalPassives(Bukkit.getPlayer(getOwner())));

        return passiveList;

    }

    public RPGClass getRPGClass() {

        return data.containsKey("rpgclass") ? core.rpgClassManager.getRPGClass(data.getString("rpgclass")) : null;

    }

    public void setRPGClass(RPGClass rpgClass) {

        data.put("rpgclass", rpgClass.getClassName());

        this.passives = rpgClass.getPassives();
        playerStats.setMaxHealth(rpgClass.getMaxHealth());
        playerStats.setHealth(playerStats.getHealthScale() * Bukkit.getPlayer(getOwner()).getHealth());

        playerStats.getLevelInfo();

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

        if (getRPGClass() == null)
            new SelectClassGUI(player);
        else {

            this.passives = getRPGClass().getPassives();

            if (passives == null)
                passives = new ArrayList<>();

            for (Passive passive : passives)
                passive.setProfile(this);

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

