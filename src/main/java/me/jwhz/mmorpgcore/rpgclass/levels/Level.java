package me.jwhz.mmorpgcore.rpgclass.levels;

import me.jwhz.mmorpgcore.events.PlayerLevelUpEvent;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import org.bson.Document;
import org.bukkit.Bukkit;

public class Level {

    private Profile profile;
    private Document document;

    private LevelSystem levelSystem;

    public Level(Profile profile, Document document) {

        this.profile = profile;
        this.document = document;

        this.levelSystem = profile.getRPGClass().getLevelSystem();

    }

    public Level(Profile profile) {

        this.profile = profile;

        Document document = new Document();

        document.put("total xp", 0l);
        document.put("current xp", 0l);
        document.put("level", 1);

        this.document = document;

        this.levelSystem = profile.getRPGClass().getLevelSystem();

    }

    public int getNextLevel() {

        return getLevel() + 1 > getLevelSystem().getMaxLevel() ? getLevel() : getLevel() + 1;

    }

    public int getLevel() {

        return document.getInteger("level");

    }

    public long getTotalXp() {

        return document.getLong("total xp");

    }

    public long getCurrentXp() {

        return document.getLong("current xp");

    }

    public void addXp(long xp) {

        document.put("total xp", getTotalXp() + xp);

        long currentXp = getCurrentXp() + xp;
        long levelRequirement = levelSystem.getLevelRequirement(getLevel() + 1);

        if (currentXp > levelRequirement) {

            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(DBPlayer.getPlayer(profile.getOwner()), this));

            document.put("current xp", currentXp - levelRequirement);
            document.put("level", getLevel() + 1);

        } else
            document.put("current xp", currentXp);

    }

    public Document getDocument() {

        return document;

    }

    public LevelSystem getLevelSystem() {

        return levelSystem;

    }

}
