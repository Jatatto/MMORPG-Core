package me.jwhz.mmorpgcore.utils;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTHelper {

    public static ItemStack addTag(String key, Object value, ItemStack item) {

        net.minecraft.server.v1_15_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tags = itemStack.getOrCreateTag();

        if (value instanceof Double)
            tags.setDouble(key, (Double) value);
        else if (value instanceof String)
            tags.setString(key, (String) value);
        else if (value instanceof Integer)
            tags.setInt(key, (Integer) value);
        else if (value instanceof Boolean)
            tags.setBoolean(key, (Boolean) value);
        else if (value instanceof Long)
            tags.setLong(key, (Long) value);

        return (item = CraftItemStack.asBukkitCopy(itemStack));

    }

    public static int getInteger(String key, ItemStack item) {

        NBTTagCompound tags = CraftItemStack.asNMSCopy(item).getOrCreateTag();

        if (tags.hasKey(key))
            return tags.getInt(key);

        return 1;

    }

    public static long getLong(String key, ItemStack item) {

        NBTTagCompound tags = CraftItemStack.asNMSCopy(item).getOrCreateTag();

        if (tags.hasKey(key))
            return tags.getLong(key);

        return 0l;

    }

    public static double getDouble(String key, ItemStack item) {

        NBTTagCompound tags = CraftItemStack.asNMSCopy(item).getOrCreateTag();

        if (tags.hasKey(key))
            return tags.getDouble(key);

        return 1.0;

    }

    public static String getString(String key, ItemStack item) {

        NBTTagCompound tags = CraftItemStack.asNMSCopy(item).getOrCreateTag();

        if (tags.hasKey(key))
            return tags.getString(key);

        return "";

    }

    public static boolean getBoolean(String key, ItemStack item) {

        NBTTagCompound tags = CraftItemStack.asNMSCopy(item).getOrCreateTag();

        if (tags.hasKey(key))
            return tags.getBoolean(key);

        return false;

    }

    public static boolean hasTag(String key, ItemStack item) {

        return CraftItemStack.asNMSCopy(item).getOrCreateTag().hasKey(key);

    }

}
