package me.jwhz.mmorpgcore;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
import org.bukkit.entity.Player;

public class MMORPGCorePlaceholderExpansion extends PlaceholderExpansion {

    private MMORPGCore core;

    public MMORPGCorePlaceholderExpansion(MMORPGCore core) {

        this.core = core;

        PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);

    }

    @Override
    public boolean canRegister() {

        return true;

    }

    @Override
    public String getIdentifier() {

        return "mmorpg";

    }

    @Override
    public String getPlugin() {

        return core.getDescription().getName();

    }

    @Override
    public String getAuthor() {

        return core.getDescription().getAuthors().get(0);

    }

    @Override
    public String getVersion() {

        return core.getDescription().getVersion();

    }

    @Override
    public String onPlaceholderRequest(Player player, String value) {

        DBPlayer dbPlayer = DBPlayer.getPlayer(player);

        if (value.equalsIgnoreCase("max_health"))
            return "" + (int) dbPlayer.getCurrentProfile().getPlayerStats().getMaxHealth();

        if (value.equalsIgnoreCase("health"))
            return "" + (int) dbPlayer.getCurrentProfile().getPlayerStats().getHealth();

        if (value.equalsIgnoreCase("mana"))
            return "" + (int) dbPlayer.getCurrentProfile().getPlayerStats().getMana();

        RPGClass rpgClass = dbPlayer.getCurrentProfile().getRPGClass();

        if (value.equalsIgnoreCase("mana_name"))
            return rpgClass != null ? rpgClass.getManaSettings().getManaName() : "Mana";

        if (value.equalsIgnoreCase("max_mana"))
            return "" + (int) (rpgClass != null ? rpgClass.getManaSettings().getMaxMana() : 100);

        if (value.equalsIgnoreCase("mana_regeneration"))
            return "" + (rpgClass != null ? rpgClass.getManaSettings().getManaRegeneration() : 5);

        if (value.equalsIgnoreCase("class"))
            return rpgClass != null ? rpgClass.getClassName() : "";

        if (value.equalsIgnoreCase("profilecount"))
            return "" + dbPlayer.getProfiles().size();

        if (value.startsWith("profile") && value.contains("_")) {

            String[] parts = value.split("_");

            Profile profile = null;

            int profileNumber = -1;

            try {

                profileNumber = Integer.parseInt(parts[1]);

            } catch (Exception ignore) {
            }

            if (profileNumber != -1 && dbPlayer.getProfiles().size() >= profileNumber)
                profile = dbPlayer.getProfiles().get(dbPlayer.getProfiles().size() - profileNumber);

            if (profile != null && parts.length > 2)
                if (parts[2].equalsIgnoreCase("name"))
                    return profile.getProfileName();
                else if (parts[2].equalsIgnoreCase("uuid"))
                    return profile.getProfileUUID().toString();

        }

        if (value.startsWith("currentprofile") && value.contains("_")) {

            Profile profile = dbPlayer.getCurrentProfile();
            String[] parts = value.split("_");

            if (profile != null)
                if (parts[1].equalsIgnoreCase("name"))
                    return profile.getProfileName();
                else if (parts[1].equalsIgnoreCase("uuid"))
                    return profile.getProfileUUID().toString();

        }

        return null;

    }

}
