package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import me.jwhz.mmorpgcore.utils.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TotemManager extends Manager<Totem> implements Listener {

    public TotemManager() {

        super("totems");

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    public void run() {

        Iterator<Totem> totems = getList().iterator();

        while (totems.hasNext()) {

            Totem totem = totems.next();

            if (totem.getItemEntity() == null)
                totems.remove();
            else
                totem.tick();

        }

    }

    public TotemConfiguration getTotemConfiguration(String string) {

        return getTotemConfigurations().stream().filter(totem -> totem.getName().equalsIgnoreCase(string)).findFirst().orElse(null);

    }

    public TotemConfiguration getTotemConfiguration(ItemStack item) {

        return getTotemConfiguration(NBTHelper.getString("totem", item));

    }


    public boolean isTotemConfiguration(String string) {

        return getTotemConfigurations().stream().anyMatch(totem -> totem.getName().equalsIgnoreCase(string));

    }

    public List<TotemConfiguration> getTotemConfigurations() {

        List<TotemConfiguration> totems = new ArrayList<>();

        if (yamlConfiguration.isSet("Totems"))
            for (String key : yamlConfiguration.getConfigurationSection("Totems").getKeys(false))
                totems.add(new TotemConfiguration(key, yamlConfiguration.getConfigurationSection("Totems." + key)));

        return totems;

    }

    public boolean hasTotemPlaced(Player player) {

        return getList().stream().anyMatch(totem -> player.equals(totem.getPlayer()));

    }

    public Totem getPlacedTotem(Player player) {

        return getList().stream().filter(totem -> player.equals(totem.getPlayer())).findFirst().orElse(null);

    }

    public boolean isTotem(ItemStack item) {

        return NBTHelper.hasTag("totem", item);

    }

    public void placeTotem(Player player, ItemStack item) {

        Item entity = player.getWorld().dropItem(player.getLocation(), item);

        entity.setVelocity(new Vector());
        entity.setGravity(false);

        TotemConfiguration totem = getTotemConfiguration(NBTHelper.getString("totem", item));

        getList().add(new Totem(totem, player, entity));

    }

    public Totem getTotem(Player player, Entity entity) {

        return getList().stream().filter(totem -> player.equals(totem.getPlayer()) && entity.equals(totem.getItemEntity())).findFirst().orElse(null);

    }

    public List<Passive> getAdditionalPassives(Player player) {

        Totem totem = null;
        int priority = 0;

        for (Totem t : getList())
            if (t.isWithin(player) && t.getTotemConfiguration().getPriorityStatus() > priority) {

                totem = t;
                priority = t.getTotemConfiguration().getPriorityStatus();

            }

        List<Passive> passives = new ArrayList<>();

        if (totem != null)
            for (Passive passive : totem.getTotemConfiguration().getBuffs()) {

                passive.setProfile(DBPlayer.getPlayer(player).getCurrentProfile());
                passives.add(passive);

            }

        return passives;

    }

    @EventHandler
    public void onTotemPickup(PlayerPickupItemEvent e) {

        for (Totem totem : getList())
            if (e.getItem().equals(totem.getItemEntity())) {

                if (!totem.canPickup())
                    e.setCancelled(true);
                else if (!totem.getPlayer().equals(e.getPlayer()))
                    e.setCancelled(System.currentTimeMillis() < totem.getOthersPickupTime());

                if (!e.isCancelled())
                    totem.setItemEntity(null);

                return;

            }

    }

    @EventHandler
    public void onPlaceTotem(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {

            if (e.getItem() != null && isTotem(e.getItem()) && !hasTotemPlaced(e.getPlayer())) {

                e.setCancelled(true);
                placeTotem(e.getPlayer(), e.getItem());

                e.getPlayer().getInventory().removeItem(e.getItem());

            }

        }
/*

        if (hasTotemPlaced(e.getPlayer())) {

            List<Entity> nearby = e.getPlayer().getNearbyEntities(2, 2, 2);

            for (Entity near : nearby)
                if (near instanceof Item) {

                    Totem totem = getTotem(e.getPlayer(), near);

                    e.setCancelled(true);

                    totem.pickup();

                    if (totem.getMana() > 0) {

                        DBPlayer dbPlayer = DBPlayer.getPlayer(e.getPlayer());

                        dbPlayer.getCurrentProfile().getPlayerStats().setMana(Math.min(dbPlayer.getCurrentProfile().getPlayerStats().getMaxHealth() + totem.getMana(),
                                dbPlayer.getCurrentProfile().getRPGClass().getManaSettings().getMaxMana()));

                    }

                    if (e.getPlayer().getInventory().firstEmpty() != -1) {

                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', totem.getTotemConfiguration().getTotemReturnedMessage()));

                        e.getPlayer().getInventory().addItem(totem.getTotemConfiguration().getItem());
                        totem.getItemEntity().remove();
                        totem.setItemEntity(null);

                    } else
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', totem.getTotemConfiguration().getOutOfManaMessage()));

                    return;

                }

        }
*/

    }


}

