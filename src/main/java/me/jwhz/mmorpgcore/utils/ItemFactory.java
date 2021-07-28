package me.jwhz.mmorpgcore.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemFactory {

    public static ItemStack build(Material material, int amount, String displayName, List<String> enchants, int customModel, boolean hideEnchants, String... lore) {

        ItemStack item = new ItemStack(material);
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

        if (hideEnchants)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        if (customModel != -1)
            meta.setCustomModelData(-1);

        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack fakeGlow(Material material, String displayName, String... lore) {

        return build(material, 1, displayName, Arrays.asList(Enchantment.DURABILITY.getName() + ",1"), -1, true, lore);

    }

    public static ItemStack build(Material material, String displayName, String... lore) {

        return build(material, 1, displayName, new ArrayList<>(), -1, false, lore);

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

    public static ItemStack replaceVariables(Player player, ItemStack item, Map<String, String> variables) {

        ItemStack itemStack = item.clone();

        if (!item.hasItemMeta())
            return itemStack;

        ItemMeta meta = itemStack.getItemMeta();

        if (meta.hasDisplayName()) {

            String name = meta.getDisplayName();

            for (Map.Entry<String, String> entry : variables.entrySet())
                name = name.replace(entry.getKey(), entry.getValue());

            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, name));

        }

        if (meta.hasLore()) {

            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore()) {

                for (Map.Entry<String, String> entry : variables.entrySet())
                    line = line.replace(entry.getKey(), entry.getValue());

                lore.add(PlaceholderAPI.setPlaceholders(player, line));

            }

            meta.setLore(lore);

        }

        itemStack.setItemMeta(meta);

        return itemStack;

    }

    public static ItemStack replaceVariable(Player player, ItemStack item, String key, String value) {

        Map<String, String> variables = new HashMap<>();
        variables.put(key, value);

        return replaceVariables(player, item, variables);

    }

}
