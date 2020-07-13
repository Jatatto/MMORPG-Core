package me.jwhz.mmorpgcore.rpgclass.passive;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.events.ManaConsumeEvent;
import me.jwhz.mmorpgcore.events.ManaRegenerationEvent;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.DamageReductionPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaReductionPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaRegenerationPassive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player)
            for (Passive passive : DBPlayer.getPlayer((Player) e.getEntity()).getCurrentProfile().getPassives())
                if (passive instanceof DamageReductionPassive)
                    ((EventPassive) passive).handle(e);

    }

}
