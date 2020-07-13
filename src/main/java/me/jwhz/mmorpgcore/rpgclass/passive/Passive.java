package me.jwhz.mmorpgcore.rpgclass.passive;

import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.totems.Totem;
import me.jwhz.mmorpgcore.totems.TotemConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public abstract class Passive<T> {

    protected Profile profile;
    protected ConfigurationSection section;

    public Passive(ConfigurationSection section) {

        this.section = section;

    }

    public Profile getProfile() {

        return profile;

    }

    public void setProfile(Profile profile) {

        this.profile = profile;

    }

    public ConfigurationSection getSection() {

        return section;

    }

}
