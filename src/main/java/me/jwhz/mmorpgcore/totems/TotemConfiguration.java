package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import me.jwhz.mmorpgcore.rpgclass.passive.PassiveType;
import org.bukkit.configuration.ConfigurationSection;

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

    public String getOutOfManaMessage() {

        return section.getString("out of mana message", "&aYour totem ran out of mana, pick it up before other players can.");

    }

    public List<Passive> getBuffs() {

        List<Passive> buffs = new ArrayList<>();

        if (section.isSet("buffs"))
            for (String key : section.getConfigurationSection("buffs").getKeys(false)) {

                ConfigurationSection configurationSection = section.getConfigurationSection("passives." + key);

                if (configurationSection.isSet("passive-type")) {

                    PassiveType type = PassiveType.getByName(configurationSection.getString("passive-type"));

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
