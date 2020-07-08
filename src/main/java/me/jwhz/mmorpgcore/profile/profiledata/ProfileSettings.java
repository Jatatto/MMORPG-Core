package me.jwhz.mmorpgcore.profile.profiledata;

import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaSettings;
import org.bson.Document;
import org.bukkit.entity.Player;

public class ProfileSettings extends ProfileData {

    public ProfileSettings(Profile profile, Document document) {

        super(profile, document, "profile settings");

    }

    @Override
    public void load(Player player) {


    }

    @Override
    public void unload(Player player) {

    }

    @Override
    public void save(Player player) {

        super.save();

    }

}
