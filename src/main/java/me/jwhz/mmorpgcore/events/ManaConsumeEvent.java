package me.jwhz.mmorpgcore.events;

import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ManaConsumeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private DBPlayer player;
    private double amount;

    public ManaConsumeEvent(DBPlayer dbPlayer, double amount) {

        this.player = dbPlayer;
        this.amount = amount;

    }

    public DBPlayer getPlayer() {

        return player;

    }

    public double getAmount() {

        return amount;

    }

    public void setAmount(double amount) {

        this.amount = amount;

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
