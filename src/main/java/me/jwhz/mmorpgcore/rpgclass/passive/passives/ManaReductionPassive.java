package me.jwhz.mmorpgcore.rpgclass.passive.passives;

import me.jwhz.mmorpgcore.events.ManaConsumeEvent;
import me.jwhz.mmorpgcore.rpgclass.passive.EventPassive;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public class ManaReductionPassive extends EventPassive<Double> {

    private double reduction;

    public ManaReductionPassive(ConfigurationSection section) {

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

        ManaConsumeEvent manaConsumeEvent = (ManaConsumeEvent) event;

        if (!manaConsumeEvent.isCancelled())
            manaConsumeEvent.setAmount(manaConsumeEvent.getAmount() * (1 - reduction));

    }


}
