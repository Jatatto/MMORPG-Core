package me.jwhz.mmorpgcore.rpgclass.health;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthManager extends Manager implements Listener {

    public HealthManager() {

        super("config");

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {

        if (e.isCancelled() || !(e.getEntity() instanceof Player))
            return;

        DBPlayer dbPlayer = DBPlayer.getPlayer((Player) e.getEntity());

        if (dbPlayer != null && dbPlayer.getCurrentProfile() != null) {

            dbPlayer.getCurrentProfile().getPlayerStats().setHealth(((Player) e.getEntity()).getHealth() - (e.getFinalDamage() / dbPlayer.getCurrentProfile().getPlayerStats().getHealthScale()));
            e.setDamage(0);

        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHealthRegen(EntityRegainHealthEvent e) {

        if (e.isCancelled() || !(e.getEntity() instanceof Player))
            return;

        DBPlayer dbPlayer = DBPlayer.getPlayer((Player) e.getEntity());

        e.setCancelled(true);

        if (dbPlayer != null && dbPlayer.getCurrentProfile() != null)
            dbPlayer.getCurrentProfile().getPlayerStats().setHealth(((Player) e.getEntity()).getHealth() + e.getAmount());

    }

}
