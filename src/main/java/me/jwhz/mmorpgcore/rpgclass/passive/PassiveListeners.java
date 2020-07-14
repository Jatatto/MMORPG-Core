package me.jwhz.mmorpgcore.rpgclass.passive;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.events.ManaConsumeEvent;
import me.jwhz.mmorpgcore.events.ManaRegenerationEvent;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaReductionPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaRegenerationPassive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PassiveListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onManaConsume(ManaConsumeEvent e) {

        for (Passive passive : e.getPlayer().getCurrentProfile().getPassives())
            if (passive instanceof ManaReductionPassive)
                ((EventPassive) passive).handle(e);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onManaRegeneration(ManaRegenerationEvent e) {

        for (Passive passive : e.getPlayer().getCurrentProfile().getPassives())
            if (passive instanceof ManaRegenerationPassive)
                ((EventPassive) passive).handle(e);

        if (MMORPGCore.getInstance().totemManager.hasTotemPlaced(e.getPlayer().getPlayer()) &&
                !MMORPGCore.getInstance().totemManager.getPlacedTotem(e.getPlayer().getPlayer()).getTotemConfiguration().canRegenerationMana())
            e.setCancelled(true);

    }


}
