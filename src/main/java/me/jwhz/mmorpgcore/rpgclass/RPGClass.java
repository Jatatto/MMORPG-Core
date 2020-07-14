package me.jwhz.mmorpgcore.rpgclass;

import me.jwhz.mmorpgcore.manager.ManagerObject;
import me.jwhz.mmorpgcore.rpgclass.levels.LevelSystem;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaSettings;
import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import me.jwhz.mmorpgcore.rpgclass.passive.PassiveType;
import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import me.jwhz.mmorpgcore.utils.materials.UMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RPGClass extends ManagerObject<File> {

    private File file;
    private YamlConfiguration yamlConfiguration;
    private LevelSystem levelSystem;

    public RPGClass(File file) {

        this.file = file;
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        getLevelSystem().getLevelRequirement(getLevelSystem().getMaxLevel());

    }

    public String getClassName() {

        return yamlConfiguration.getString("class name", file.getName().replace(".yml", ""));

    }

    public ItemStack getItem() {

        if (yamlConfiguration.isSet("gui item"))
            return BukkitSerialization.convertSection(yamlConfiguration.getConfigurationSection("gui item"));

        return ItemFactory.build(UMaterial.PAPER, getClassName(), "Edited in config....");

    }

    public LevelSystem getLevelSystem() {

        return levelSystem != null ?
                levelSystem :
                (levelSystem = (yamlConfiguration.isSet("level system") ?
                        new LevelSystem(yamlConfiguration.getConfigurationSection("level system")) :
                        new LevelSystem("1.25(n-1)", 50, 100)
                ));

    }

    public double getMaxHealth() {

        return yamlConfiguration.getDouble("max health", 20.0);

    }

    public List<Passive> getPassives() {

        List<Passive> passives = new ArrayList<>();

        if (yamlConfiguration.isSet("passives"))
            for (String key : yamlConfiguration.getConfigurationSection("passives").getKeys(false)) {

                ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("passives." + key);
                if (configurationSection.isSet("type")) {

                    PassiveType type = PassiveType.getByName(configurationSection.getString("type"));

                    try {

                        passives.add((Passive) type.getPassiveClass().getConstructor(ConfigurationSection.class).newInstance(configurationSection));

                    } catch (Exception ignore) {

                    }

                }

            }

        return passives;

    }

    public ManaSettings getManaSettings() {

        if (yamlConfiguration.isSet("mana"))
            return new ManaSettings(yamlConfiguration.getConfigurationSection("mana"));

        return new ManaSettings(100, 10, "Mana");

    }

    @Override
    public File getIdentifier() {

        return file;

    }

}
