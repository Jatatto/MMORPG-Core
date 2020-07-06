package me.jwhz.mmorpgcore.rpgclass.passive;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public abstract class TickPassive<T> extends Passive {

    protected List<T> buffs = new ArrayList<>();

    public TickPassive(ConfigurationSection section) {
        super(section);
    }

    public abstract void onTick();

    public abstract void applyBuff(T t);

    public abstract void removeBuff(T t);

}
