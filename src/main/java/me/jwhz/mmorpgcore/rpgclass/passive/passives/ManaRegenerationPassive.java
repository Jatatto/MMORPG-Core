package me.jwhz.mmorpgcore.rpgclass.passive.passives;

import me.jwhz.mmorpgcore.events.ManaRegenerationEvent;
import me.jwhz.mmorpgcore.rpgclass.passive.EventPassive;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public class ManaRegenerationPassive extends EventPassive<Double> {

    private double regenerationBoost;

    public ManaRegenerationPassive(ConfigurationSection section) {

        super(section);

        this.regenerationBoost = section.getDouble("regeneration boost");

    }

    @Override
    public void handle(Event event) {

        ManaRegenerationEvent regenerationEvent = (ManaRegenerationEvent) event;

        regenerationEvent.setRegenerationAmount(regenerationEvent.getRegenerationAmount() * (1 + regenerationBoost));

    }

    @Override
    public void applyBuff(Double buff) {

        this.regenerationBoost += buff;

        buffs.add(buff);

    }

    @Override
    public void removeBuff(Double buff) {

        this.regenerationBoost -= buff;

        buffs.remove(buff);

    }

    @Override
    public String toString() {

        return "Increase mana regeneration by " +  (regenerationBoost * 100) + "%";

    }

}
