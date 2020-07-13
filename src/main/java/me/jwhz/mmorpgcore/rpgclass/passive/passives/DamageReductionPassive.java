package me.jwhz.mmorpgcore.rpgclass.passive.passives;

import me.jwhz.mmorpgcore.rpgclass.passive.EventPassive;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageReductionPassive extends EventPassive<Double> {

    private double reduction;

    public DamageReductionPassive(ConfigurationSection section) {

        super(section);

        this.reduction = section.getDouble("reduction");

    }

    @Override
    public void applyBuff(Double buff) {

        this.reduction += buff;
        buffs.add(buff);

    }

    @Override
    public void removeBuff(Double buff) {

        this.reduction -= buff;
        buffs.remove(buff);

    }

    @Override
    public void handle(Event event) {

        EntityDamageEvent e = (EntityDamageEvent) event;

        if (!e.isCancelled())
            e.setDamage(e.getDamage() * (1 - reduction));

    }

    @Override
    public String toString() {

        return "Reduce damage by " + (reduction * 100) + "%";

    }
}
