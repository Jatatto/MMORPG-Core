package me.jwhz.mmorpgcore.gui.guis;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.gui.GUI;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import me.jwhz.mmorpgcore.utils.materials.UMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ViewInventoryContentGUI extends GUI {

    private ItemStack[] content;
    private GUI previous;

    @ConfigValue("gui.view inventory content.name")
    String name = "&aViewing items";

    @ConfigValue("gui.view inventory content.items.filler item")
    ItemStack fillerItem = ItemFactory.build(UMaterial.GRAY_STAINED_GLASS_PANE, "&f");
    @ConfigValue("gui.view inventory content.items.back arrow")
    ItemStack backArrow = ItemFactory.build(UMaterial.ARROW, "&cBack");

    public ViewInventoryContentGUI(Player player, GUI previous, ItemStack[] content) {

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        this.content = content;
        this.previous = previous;

        int row = (int) (1 + Math.round(0.5 + (content.length / 9)));

        this.inventory = Bukkit.createInventory(null, row * 9, name);

        open(player);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getSlot() == inventory.getSize() - 5)
            previous.open((Player) e.getWhoClicked());

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < content.length; i++)
            inventory.setItem(i, content[i]);

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++)
            inventory.setItem(i, fillerItem);

        inventory.setItem(inventory.getSize() - 5, backArrow);

    }

}
