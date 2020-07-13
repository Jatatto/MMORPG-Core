package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import me.jwhz.mmorpgcore.rpgclass.passive.PassiveType;
import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import me.jwhz.mmorpgcore.utils.NBTHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TotemConfiguration {

    private String key;
    private ConfigurationSection section;

    public TotemConfiguration(String key, ConfigurationSection section) {

        this.key = key;
        this.section = section;

    }

    public double getRadius() {

        return section.getDouble("radius", 20.0);

    }

    public double getManaPerSecond() {

        return section.getDouble("mana per second", 10.0);

    }

    public int getPickupDelay() {

        return section.getInt("pickup delay", 10);

    }

    public String getEntityName() {

        return section.getString("entity name", "&3&l" + key + " Totem - &b%seconds% seconds");

    }

    public String getPickupName() {

        return section.getString("entity pickup name", "&4&lPickup Totem: &cOut of mana");

    }

    public String getOutOfManaMessage() {

        return section.getString("out of mana message", "&4Your totem ran out of mana, pick it up before other players can.");

    }

    public String getTotemReturnedMessage() {

        return section.getString("totem returned message", "&aYour totem has ran out of mana and has been returned to you.");

    }

    public int getPriorityStatus() {

        return section.getInt("priority status", 100);

    }

    public Color getColor() {

        String color = section.getString("particle color", "255,0,0");

        String[] parts = color.split(",");

        return Color.fromRGB(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

    }

    public int getSize() {

        return section.getInt("particle size", 2);

    }

    public boolean canRegenerationMana(){

        return section.getBoolean("can regeneration mana", false);

    }

    public ItemStack getItem() {

        return NBTHelper.addTag("totem", getName(), section.isSet("item") ? BukkitSerialization.convertSection(section.getConfigurationSection("item")) : new ItemStack(Material.STONE));

    }

    public List<Passive> getBuffs() {

        List<Passive> buffs = new ArrayList<>();

        if (section.isSet("buffs"))
            for (String key : section.getConfigurationSection("buffs").getKeys(false)) {

                ConfigurationSection configurationSection = section.getConfigurationSection("buffs").getConfigurationSection(key);

                if (configurationSection.isSet("type")) {

                    PassiveType type = PassiveType.getByName(configurationSection.getString("type"));

                    try {

                        buffs.add((Passive) type.getPassiveClass().getConstructor(ConfigurationSection.class).newInstance(configurationSection));

                    } catch (Exception ignore) {
                    }

                }

            }

        return buffs;

    }

    public String getName() {

        return key;

    }

    public ConfigurationSection getSection() {

        return section;

    }

}
