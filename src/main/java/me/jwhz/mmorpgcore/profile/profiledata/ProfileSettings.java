package me.jwhz.mmorpgcore.profile.profiledata;

import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaSettings;
import org.bson.Document;
import org.bukkit.entity.Player;

public class ProfileSettings extends ProfileData {

    public ProfileSettings(Profile profile, Document document) {

        super(profile, document, "profile settings");

    }

    public ManaSettings getManaSettings() {

        if (document.containsKey("mana settings"))
            return new ManaSettings((Document) document.get("mana settings"));

        return new ManaSettings(100, 5);

    }

    public void setManaSettings(ManaSettings manaSettings) {

        document.put("mana settings", manaSettings.getDocument());

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
