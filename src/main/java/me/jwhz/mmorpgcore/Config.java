package me.jwhz.mmorpgcore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class Config {

    private MMORPGCore core;

    Config(MMORPGCore core) {

        this.core = core;

    }

    public Location getNewProfileSpawn() {

        return getLocation(core.getConfig().getConfigurationSection("profiles.new profile spawn"));

    }

    private Location getLocation(ConfigurationSection section) {

        return section.isSet("yaw") ?
                new Location(
                        Bukkit.getWorld(Objects.requireNonNull(section.getString("world"))),
                        section.getDouble("x"),
                        section.getDouble("y"),
                        section.getDouble("z"),
                        (float) section.getDouble("yaw"),
                        (float) section.getDouble("pitch")
                ) :
                new Location(
                        Bukkit.getWorld(Objects.requireNonNull(section.getString("world"))),
                        section.getDouble("x"),
                        section.getDouble("y"),
                        section.getDouble("z")
                );

    }

}
