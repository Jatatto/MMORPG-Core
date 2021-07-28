package me.jwhz.mmorpgcore.gui.guis;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.gui.GUI;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ViewProfileGUI extends GUI {

    @ConfigValue("gui.view profile.name")
    String name = "Viewing profile: %profile_name%";

    @ConfigValue("gui.view profile.items.filler item")
    ItemStack fillerItem = ItemFactory.build(Material.GRAY_STAINED_GLASS_PANE, "&f");
    @ConfigValue("gui.view profile.items.back arrow")
    ItemStack backArrow = ItemFactory.build(Material.ARROW, "&cBack");
    @ConfigValue("gui.view profile.items.enderchest")
    ItemStack enderChest = ItemFactory.build(Material.ENDER_CHEST, "&bEnder Chest", "", "&7Click to view enderchest");
    @ConfigValue("gui.view profile.items.player info")
    ItemStack playerInfo = ItemFactory.build(Material.PAPER, "&aPlayer info",
            "",
            "&7- XP Level: &b%profile_exp_level% (%profile_current_xp% exp)",
            "&7- Location: &b%profile_x%&7, &b%profile_y%&7, &b%profile_z%"
    );

    private Profile profile;

    public ViewProfileGUI(Player player, Profile profile) {

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        this.profile = profile;

        this.inventory = Bukkit.createInventory(null, 54, name.replace("%profile_name%", profile.getProfileName()));

        if (DBPlayer.getPlayer(player).getCurrentProfile().getProfileUUID().equals(profile.getProfileUUID()))
            profile.getPlayerStats().save(player);

        open(player);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getSlot() == 45)
            new ProfileSelectGUI((Player) e.getWhoClicked());
        else if (e.getSlot() == 47)
            new ViewInventoryContentGUI((Player) e.getWhoClicked(), this, profile.getPlayerStats().getEnderChest());

    }

    @Override
    public void setupGUI(Player player) {

        ItemStack[] items = profile.getPlayerStats().getInventory();

        if (items != null) {

            for (int i = 0; i < 9; i++)
                inventory.setItem(i + 27, items[i]);

            for (int i = 9; i < 36; i++)
                inventory.setItem(i - 9, items[i]);

        }

        for (int i = 36; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemFactory.replacePlaceholders(player, fillerItem));

        inventory.setItem(45, ItemFactory.replacePlaceholders(player, backArrow));

        inventory.setItem(47, ItemFactory.replacePlaceholders(player, enderChest));

        ItemStack info = playerInfo.clone();

        ItemMeta meta = info.getItemMeta();

        if (meta.hasDisplayName())
            meta.setDisplayName(replaceVariables(meta.getDisplayName()));

        if (meta.hasLore()) {

            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore())
                lore.add(replaceVariables(line));

            meta.setLore(lore);

        }

        info.setItemMeta(meta);

        inventory.setItem(48, info);

        if (items != null) {

            ItemStack[] armor = new ItemStack[]{items[39], items[38], items[37], items[36]};

            for (int i = 0; i < armor.length; i++)
                inventory.setItem(50 + i, armor[i]);

        }

    }

    private String replaceVariables(String string) {

        Location location = profile.getPlayerStats().getLocation();

        return string.replace("%profile_exp_level%", profile.getPlayerStats().getExp().getInteger("xp level") + "")
                .replace("%profile_current_xp%", profile.getPlayerStats().getExp().getString("current xp"))
                .replace("%profile_x%", location.getBlockX() + "")
                .replace("%profile_y%", location.getBlockY() + "")
                .replace("%profile_z%", location.getBlockZ() + ""
                );

    }

}
