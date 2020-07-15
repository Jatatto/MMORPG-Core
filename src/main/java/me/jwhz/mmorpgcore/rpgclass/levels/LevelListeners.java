package me.jwhz.mmorpgcore.rpgclass.levels;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.events.PlayerLevelUpEvent;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class LevelListeners implements Listener {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    private DecimalFormat decimalFormat = new DecimalFormat("#,###.#");
    private MMORPGCore core;

    public LevelListeners(MMORPGCore core) {

        this.core = core;

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityHit(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof ArmorStand) && e.getDamager() instanceof Player && !e.isCancelled()) {

            double angle = Math.random() * (Math.PI * 2);

            double yRemove = random.nextDouble(0.3, 1.1);

            ArmorStand armorStand = (ArmorStand) e.getDamager().getWorld().spawnEntity(e.getEntity().getLocation().add(
                    Math.cos(angle),
                    0,
                    Math.sin(angle)
            ).subtract(0, yRemove, 0), EntityType.ARMOR_STAND);

            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCanPickupItems(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCollidable(false);

            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&',
                    core.config.getDamageHologramText().replace("%damage%", decimalFormat.format(e.getFinalDamage()))
            ));

            core.rpgClassManager.addArmorStand(armorStand, System.currentTimeMillis() + (1000));

        }

    }

    @EventHandler
    public void onMythicMobKill(MythicMobDeathEvent e) {

        if (e.getKiller() instanceof Player) {

            DBPlayer player = DBPlayer.getPlayer((Player) e.getKiller());

            player.getCurrentProfile().getPlayerStats().getLevelInfo().addXp((long) e.getMobLevel());

        }

    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {

        if (e.getEntity().getKiller() != null) {

            DBPlayer player = DBPlayer.getPlayer(e.getEntity().getKiller());

            player.getCurrentProfile().getPlayerStats().getLevelInfo().addXp(e.getDroppedExp());

        }

    }

    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent e) {

        e.getPlayer().sendMessage(core.messages.levelUp);

    }

}
