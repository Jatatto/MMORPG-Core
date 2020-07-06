package me.jwhz.mmorpgcore.events;

import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ManaRegenerationEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private DBPlayer player;
    private double regenerationAmount;

    public ManaRegenerationEvent(DBPlayer dbPlayer, double regenerationAmount) {

        this.player = dbPlayer;
        this.regenerationAmount = regenerationAmount;

    }

    public DBPlayer getPlayer() {

        return player;

    }

    public double getRegenerationAmount() {

        return regenerationAmount;

    }

    public void setRegenerationAmount(double regenerationAmount) {

        this.regenerationAmount = regenerationAmount;

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
