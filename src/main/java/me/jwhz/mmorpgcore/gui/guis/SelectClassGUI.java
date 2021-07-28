package me.jwhz.mmorpgcore.gui.guis;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.gui.GUI;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SelectClassGUI extends GUI {

    private Map<ItemStack, RPGClass> classes = new HashMap<>();

    @ConfigValue("gui.select class.rows")
    int rows = 3;
    @ConfigValue("gui.select class.name")
    String name = "&aSelect a class";
    @ConfigValue("gui.select class.items.filler item")
    ItemStack fillerItem = ItemFactory.build(Material.GRAY_STAINED_GLASS_PANE, "&f");

    @ConfigValue("gui.select class.slots")
    Map<String, String> slots = getDefaultSlots();

    public SelectClassGUI(Player player) {

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        this.inventory = Bukkit.createInventory(null, rows * 9, name);

        open(player);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (classes.containsKey(e.getCurrentItem())) {

            RPGClass rpgClass = classes.get(e.getCurrentItem());

            DBPlayer.getPlayer(e.getWhoClicked().getUniqueId()).getCurrentProfile().setRPGClass(rpgClass);

            e.getWhoClicked().sendMessage(core.getMessages().classSelected.replace("%class%", rpgClass.getClassName()));

            e.getWhoClicked().closeInventory();

        }

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

        DBPlayer dbPlayer = DBPlayer.getPlayer((Player) e.getPlayer());

        if (dbPlayer != null && dbPlayer.getCurrentProfile() != null && dbPlayer.getCurrentProfile().getRPGClass() == null)
            Bukkit.getScheduler().runTaskLater(core, () -> {
                new SelectClassGUI((Player) e.getPlayer());
                e.getPlayer().sendMessage(core.getMessages().selectClass);
            }, 1);

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemFactory.replacePlaceholders(player, fillerItem));

        for (Map.Entry<String, String> entry : slots.entrySet())

            if (core.getRpgClassManager().isRPGClass(entry.getValue())) {

                RPGClass rpgClass = core.getRpgClassManager().getRPGClass(entry.getValue());

                ItemStack item = ItemFactory.replacePlaceholders(player, rpgClass.getItem());

                inventory.setItem(Integer.parseInt(entry.getKey()), item);

                classes.put(item, rpgClass);

            }

    }

    private Map<String, String> getDefaultSlots() {

        Map<String, String> slots = new HashMap<>();

        slots.put("11", "Mage");
        slots.put("13", "Warrior");
        slots.put("15", "Archer");

        return slots;

    }

}
