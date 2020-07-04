package me.jwhz.mmorpgcore.config;

import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {

    public static void reload(Object clazz, File file) {

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (Field f : clazz.getClass().getDeclaredFields()) {

            f.setAccessible(true);

            if (f.isAnnotationPresent(ConfigValue.class)) {

                ConfigValue configAnnotation = f.getAnnotation(ConfigValue.class);

                if (!configuration.isSet(configAnnotation.value()))
                    continue;

                try {

                    Object value = configuration.get(configAnnotation.value());

                    if (f.getType().isInstance(new ItemStack(Material.STONE)))
                        f.set(clazz, BukkitSerialization.convertSection(configuration.getConfigurationSection(configAnnotation.value())));
                    else if (f.getType().isInstance(new HashMap<>()))
                        f.set(clazz, toMap(configuration.getConfigurationSection(configAnnotation.value())));
                    else if (value instanceof String)
                        f.set(clazz, ChatColor.translateAlternateColorCodes('&', String.valueOf(value)));
                    else
                        f.set(clazz, value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static Map<String, String> toMap(ConfigurationSection section) {

        Map<String, String> map = new HashMap<>();

        for (String key : section.getKeys(false))
            map.put(key, section.getString(key));

        return map;

    }

    public static void setPresets(Object clazz, File file) {

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (Field f : clazz.getClass().getDeclaredFields()) {

            f.setAccessible(true);

            if (f.isAnnotationPresent(ConfigValue.class)) {

                ConfigValue configAnnotation = f.getAnnotation(ConfigValue.class);

                try {
                    Object value = f.get(clazz);

                    if (value instanceof ItemStack)
                        configuration.set(configAnnotation.value(), BukkitSerialization.convertItem((ItemStack) value));
                    else if (!configuration.isSet(configAnnotation.value()))
                        configuration.set(configAnnotation.value(), value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void reload(ConfigFile configFile) {

        reload(configFile, configFile.getFile());

    }

    public static void setPresets(ConfigFile configFile) {

        setPresets(configFile, configFile.getFile());

    }

}