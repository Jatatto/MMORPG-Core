package me.jwhz.mmorpgcore.rpgclass;

import me.jwhz.mmorpgcore.config.ConfigFile;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaSettings;

public abstract class RPGClass extends ConfigFile {

    public RPGClass(String fileName) {

        super(fileName);

    }

    public ManaSettings getManaSettings() {

        if (getYamlConfiguration().isSet("mana"))
            return new ManaSettings(getYamlConfiguration().getConfigurationSection("mana"));

        return new ManaSettings(100, 10);

    }

}
