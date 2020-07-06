package me.jwhz.mmorpgcore;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
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

        if (value.equalsIgnoreCase("mana"))
            return "" + (int) dbPlayer.getCurrentProfile().getPlayerStats().getMana();

        if (value.equalsIgnoreCase("max_mana"))
            return "" + (int) dbPlayer.getCurrentProfile().getProfileSettings().getManaSettings().getMaxMana();

        if (value.equalsIgnoreCase("mana_regeneration"))
            return "" + dbPlayer.getCurrentProfile().getProfileSettings().getManaSettings().getManaRegeneration();

        if (value.startsWith("profile") && value.contains("_")) {

            String[] parts = value.split("_");

            Profile profile = null;

            int profileNumber = -1;

            try {

                profileNumber = Integer.parseInt(parts[1]);

            } catch (Exception e) {
            }

            if (profileNumber != -1 && dbPlayer.getProfiles().size() >= profileNumber)
                profile = dbPlayer.getProfiles().get(dbPlayer.getProfiles().size() - profileNumber);

            if (profile != null && parts.length > 2) {

                if (parts[2].equalsIgnoreCase("name"))
                    return profile.getProfileName();
                else if (parts[2].equalsIgnoreCase("uuid"))
                    return profile.getProfileUUID().toString();

            }

        }

        if (value.startsWith("currentprofile") && value.contains("_")) {

            Profile profile = dbPlayer.getCurrentProfile();
            String[] parts = value.split("_");

            if (profile != null) {

                if (parts[1].equalsIgnoreCase("name"))
                    return profile.getProfileName();
                else if (parts[1].equalsIgnoreCase("uuid"))
                    return profile.getProfileUUID().toString();

            }


        }

        return null;

    }

}
