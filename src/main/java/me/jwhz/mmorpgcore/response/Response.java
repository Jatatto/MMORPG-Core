package me.jwhz.mmorpgcore.response;

import me.jwhz.mmorpgcore.MMORPGCore;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Response<T> {

    protected Listener listener;

    public void register() {

        Bukkit.getPluginManager().registerEvents(listener, MMORPGCore.getInstance());

    }

    public void unreigster(){

        HandlerList.unregisterAll(listener);

    }

    public abstract boolean onResponse(T t);

}
