package me.jwhz.mmorpgcore.events;

import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.rpgclass.levels.Level;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private DBPlayer player;
    private Level levelInfo;

    public PlayerLevelUpEvent(DBPlayer dbPlayer, Level levelInfo) {

        this.player = dbPlayer;
        this.levelInfo = levelInfo;

    }

    public DBPlayer getPlayer() {

        return player;

    }

    public Level getLevelInfo() {

        return levelInfo;

    }

    public void setLevelInfo(Level levelInfo) {

        this.levelInfo = levelInfo;

    }

    public HandlerList getHandlers() {

        return HANDLERS;

    }

    public static HandlerList getHandlerList() {

        return HANDLERS;

    }

}
