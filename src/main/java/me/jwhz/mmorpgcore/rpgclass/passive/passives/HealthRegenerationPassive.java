package me.jwhz.mmorpgcore.rpgclass.passive.passives;

import me.jwhz.mmorpgcore.rpgclass.passive.EventPassive;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthRegenerationPassive extends EventPassive<Double> {

    private double regenerationBoost;

    public HealthRegenerationPassive(ConfigurationSection section) {

        super(section);

        this.regenerationBoost = 1 + section.getDouble("regeneration boost", .50);

    }

    @Override
    public void handle(Event event) {

        EntityRegainHealthEvent e = (EntityRegainHealthEvent) event;

        e.setAmount(e.getAmount() * regenerationBoost);

    }

    @Override
    public void applyBuff(Double buff) {

        this.regenerationBoost += buff;
        buffs.add(buff);

    }

    @Override
    public void removeBuff(Double buff) {

        this.regenerationBoost -= buff;
        buffs.add(buff);

    }
}
