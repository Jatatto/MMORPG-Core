package me.jwhz.mmorpgcore.profile.profiledata;

import me.jwhz.mmorpgcore.profile.Profile;
import org.bson.Document;
import org.bukkit.entity.Player;

public abstract class ProfileData {

    public Profile profile;
    private String key;
    protected Document document;

    public ProfileData(Profile profile, Document document, String key) {

        this.profile = profile;
        this.key = key;
        this.document = document;

    }

    public Profile getProfile() {

        return profile;

    }

    public Document getDocument() {

        return document;

    }

    public String getKey() {

        return key;

    }

    public void save() {

        profile.getData().put(key, document);

    }

    public abstract void load(Player player);

    public abstract void unload(Player player);

    public abstract void save(Player player);

}
