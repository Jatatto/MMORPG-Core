package me.jwhz.mmorpgcore.rpgclass.levels;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.events.PlayerLevelUpEvent;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.ChatColor;
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

        if (e.getDamager() instanceof Player) {

            boolean negative = Math.random() >= .5;

            double angle = (negative ? -1 : 1) * e.getEntity().getLocation().getDirection().angle(e.getDamager().getLocation().getDirection());

            double yAdd = random.nextDouble(.5, 1.25);

            ArmorStand armorStand = (ArmorStand) e.getDamager().getWorld().spawnEntity(e.getEntity().getLocation().add(
                    e.getEntity().getWidth() * 1.5 * Math.cos(angle),
                    yAdd,
                    e.getEntity().getWidth() * 1.15 * Math.sin(angle)
            ), EntityType.ARMOR_STAND);

            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setCanPickupItems(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&',
                    core.config.getDamageHologramText().replace("%damage%", decimalFormat.format(e.getFinalDamage()))
            ));

            core.rpgClassManager.addArmorStand(armorStand, System.currentTimeMillis() + (3 * 1000));

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
