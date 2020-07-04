package me.jwhz.mmorpgcore.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jwhz.mmorpgcore.utils.materials.UMaterial;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemFactory {

    public static ItemStack build(UMaterial material, int amount, String displayName, List<String> enchants, int customModel, String... lore) {

        ItemStack item = material.getItemStack();
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        if (enchants.size() > 0)
            for (String enchant : enchants) {

                String[] parts = enchant.split(",");

                meta.addEnchant(Enchantment.getByName(parts[0]), Integer.parseInt(parts[1]), true);

            }

        if (lore != null && lore.length > 0) {

            List<String> newLore = new ArrayList<>();

            for (String string : lore)
                newLore.add(ChatColor.translateAlternateColorCodes('&', string));

            meta.setLore(newLore);

        }

        if (customModel != -1)
            meta.setCustomModelData(-1);

        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack build(UMaterial material, String displayName, String... lore) {

        return build(material, 1, displayName, new ArrayList<>(), -1, lore);

    }

    public static ItemStack replacePlaceholders(Player player, ItemStack item) {

        ItemStack itemStack = item.clone();

        if (!item.hasItemMeta())
            return itemStack;

        ItemMeta meta = itemStack.getItemMeta();

        if (meta.hasDisplayName())
            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));

        if (meta.hasLore()) {

            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore())
                lore.add(PlaceholderAPI.setPlaceholders(player, line));

            meta.setLore(lore);

        }

        itemStack.setItemMeta(meta);

        return itemStack;

    }

    public static ItemStack replaceVariables(Player player, ItemStack item, String with, String what) {

        ItemStack itemStack = item.clone();

        if (!item.hasItemMeta())
            return itemStack;

        ItemMeta meta = itemStack.getItemMeta();

        if (meta.hasDisplayName())
            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName().replace(what, with)));

        if (meta.hasLore()) {

            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore())
                lore.add(PlaceholderAPI.setPlaceholders(player, line.replace(what, with)));

            meta.setLore(lore);

        }

        itemStack.setItemMeta(meta);

        return itemStack;

    }

}
