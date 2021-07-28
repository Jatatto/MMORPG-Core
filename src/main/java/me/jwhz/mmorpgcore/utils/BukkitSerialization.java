package me.jwhz.mmorpgcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Credits:
 * Author: graywolf336 (https://gist.github.com/graywolf336)
 * Website: https://gist.github.com/graywolf336/8153678#file-bukkitserialization-java
 */
public class BukkitSerialization {

    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[]{content, armor};
    }

    /**
     * A method to serialize an {@link ItemStack} array to Base64 String.
     * <p>
     * <p/>
     * <p>
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     * <p>
     * <p/>
     * <p>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     * <p>
     * <p/>
     * <p>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode rpgclass type.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     * <p>
     * <p/>
     * <p>
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode rpgclass type.", e);
        }
    }

    /**
     * Rest written by: JakeDev
     */

    public static String locationToString(Location location) {

        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();

    }

    public static Location locationFromString(String string) {

        String[] parts = string.split(",");

        return new Location(

                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])

        );

    }

    public static Map<String, Object> convertItem(ItemStack item) {

        Map<String, Object> section = new HashMap<>();

        section.put("material", item.getType().name());
        section.put("amount", item.getAmount());

        if (item.hasItemMeta()) {

            ItemMeta itemMeta = item.getItemMeta();

            section.put("name", itemMeta.getDisplayName());
            section.put("lore", itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>());

            List<String> enchants = new ArrayList<>();

            if (itemMeta.hasEnchants())
                for (Map.Entry<Enchantment, Integer> pair : itemMeta.getEnchants().entrySet())
                    enchants.add(pair.getKey().getName() + "," + pair.getValue());

            if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
                section.put("hide enchants", true);

            section.put("enchants", enchants);

            section.put("custom model", itemMeta.hasCustomModelData() ? itemMeta.getCustomModelData() : -1);

        }

        return section;

    }

    public static ItemStack convertMap(Map<String, Object> section) {

        ItemStack item = new ItemStack(Material.valueOf(String.valueOf(section.get("material"))));
        item.setAmount((Integer) section.get("amount"));

        ItemMeta meta = item.getItemMeta();

        if (section.containsKey("name"))
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.valueOf(section.get("name"))));

        if (section.containsKey("lore")) {

            List<String> lore = new ArrayList<>();

            for (String line : (List<String>) section.get("lore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', line));

            meta.setLore(lore);

        }

        if (section.containsKey("hide enchants") && (boolean) section.get("hide enchants"))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        if (section.containsKey("enchants"))
            for (String enchant : (List<String>) section.get("enchants")) {

                String[] parts = enchant.split(",");

                meta.addEnchant(Enchantment.getByName(parts[0]), Integer.parseInt(parts[1]), true);

            }

        if (section.containsKey("custom model") && (int) section.get("custom model") != -1)
            meta.setCustomModelData((Integer) section.get("custom model"));

        item.setItemMeta(meta);

        return item;


    }

    public static ItemStack convertSection(ConfigurationSection section) {

        ItemStack item = new ItemStack(Material.valueOf(String.valueOf(section.get("material"))));
        item.setAmount(section.getInt("amount"));

        ItemMeta meta = item.getItemMeta();

        if (section.contains("name"))
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("name")));

        if (section.contains("lore")) {

            List<String> lore = new ArrayList<>();

            for (String line : section.getStringList("lore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', line));

            meta.setLore(lore);

        }

        if (section.contains("hide enchants") && section.getBoolean("hide enchants"))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        if (section.contains("enchants"))
            for (String enchant : section.getStringList("enchants")) {

                String[] parts = enchant.split(",");

                meta.addEnchant(Enchantment.getByName(parts[0]), Integer.parseInt(parts[1]), true);

            }

        if (section.contains("custom model") && section.getInt("custom model") != -1)
            meta.setCustomModelData(section.getInt("custom model"));

        item.setItemMeta(meta);

        return item;


    }

}
