package me.jwhz.mmorpgcore.config.files;

import me.jwhz.mmorpgcore.config.ConfigFile;
import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;

public class Messages extends ConfigFile {

    /**
     * Profile related messages
     */

    @ConfigValue("messages.profile.profile loaded")
    public String profileLoaded = "&aProfile loaded: %profile%";
    @ConfigValue("messages.profile.profile already exists")
    public String profileAlreadyExists = "&cThis profile already exists, try again:";
    @ConfigValue("messages.profile.profile created")
    public String profileCreated = "&a%profile% profile created.";
    @ConfigValue("messages.profile.enter a profile name")
    public String enterProfileName = "&aEnter a profile name:";
    @ConfigValue("messages.profile.maximum amount of profiles created")
    public String maximumProfilesCreated = "&cYou do not have permission to create another profile.";
    @ConfigValue("messages.profile.select a class")
    public String selectClass = "&aPlease select a class before you continue.";
    @ConfigValue("messages.profile.class selected")
    public String classSelected = "&aYou have selected the %class% class.";

    /**
     * Mana related messages
     */

    @ConfigValue("messages.mana.mana bar")
    public String manaBar = "&3Mana: &b%mmorpg_mana%/%mmorpg_max_mana%";

    public Messages() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

    }

}
