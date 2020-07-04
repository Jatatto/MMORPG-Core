package me.jwhz.mmorpgcore.gui;

import me.jwhz.mmorpgcore.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUI extends ConfigFile {

    protected Inventory inventory;
    private Listener listener;

    public GUI() {

        super("gui");

    }

    public void registerListener(final Player player) {

        listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player))
                    onClick(e);

            }

            @EventHandler
            public void onInventoryInteract(InventoryInteractEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player))
                    e.setCancelled(true);

            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {

                if (e.getInventory().equals(inventory) && e.getPlayer().equals(player)) {

                    onClose(e);
                    HandlerList.unregisterAll(this);
                    listener = null;

                }

            }
        };

    }

    public void open(Player player) {

        if (listener == null)
            registerListener(player);
        Bukkit.getPluginManager().registerEvents(listener, core);
        setupGUI(player);
        player.openInventory(inventory);

    }

    public void onClick(InventoryClickEvent e) {
    }

    public void onClose(InventoryCloseEvent e) {
    }

    public abstract void setupGUI(Player player);

}
