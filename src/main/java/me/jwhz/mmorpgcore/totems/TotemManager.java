package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.utils.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TotemManager extends Manager<Totem> implements Listener {

    public TotemManager() {

        super("totems");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> getList().forEach(Totem::tick), 0, 20);

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    public TotemConfiguration getTotemConfiguration(String string) {

        return getTotemConfigurations().stream().filter(totem -> totem.getName().equalsIgnoreCase(string)).findFirst().orElse(null);

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

    @EventHandler
    public void onPlaceTotem(PlayerInteractEvent e) {

        if (e.getItem() != null && isTotem(e.getItem())) {

            e.setCancelled(true);
            placeTotem(e.getPlayer(), e.getItem());

            e.getPlayer().getInventory().removeItem(e.getItem());

        }

    }

}
