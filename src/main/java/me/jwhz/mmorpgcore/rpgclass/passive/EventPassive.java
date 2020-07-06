package me.jwhz.mmorpgcore.rpgclass.passive;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class EventPassive<T> extends Passive {

    protected List<T> buffs = new ArrayList<>();

    public EventPassive(ConfigurationSection section) {

        super(section);

    }

    public abstract void handle(Event event);

    public abstract void applyBuff(T t);

    public abstract void removeBuff(T t);

}
