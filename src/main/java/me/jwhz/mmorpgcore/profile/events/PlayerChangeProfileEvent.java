package me.jwhz.mmorpgcore.profile.events;

import com.mongodb.lang.Nullable;
import me.jwhz.mmorpgcore.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeProfileEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private Player player;
    private Profile oldProfile, newProfile;

    public PlayerChangeProfileEvent(Player player, Profile oldProfile, Profile newProfile) {

        this.player = player;
        this.oldProfile = oldProfile;
        this.newProfile = newProfile;

    }

    public Player getPlayer() {

        return player;

    }

    @Nullable
    public Profile getOldProfile() {

        return oldProfile;

    }

    public Profile getNewProfile() {

        return newProfile;

    }


    @Override
    public boolean isCancelled() {

        return cancelled;

    }

    @Override
    public void setCancelled(boolean cancel) {

        this.cancelled = cancel;

    }

    public HandlerList getHandlers() {

        return HANDLERS;

    }

    public static HandlerList getHandlerList() {

        return HANDLERS;

    }

}
