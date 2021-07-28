package me.jwhz.mmorpgcore;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import de.erethon.dungeonsxl.DungeonsXL;
import me.jwhz.mmorpgcore.command.CommandManager;
import me.jwhz.mmorpgcore.config.files.Config;
import me.jwhz.mmorpgcore.config.files.Messages;
import me.jwhz.mmorpgcore.database.MongoDB;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.PlayerManager;
import me.jwhz.mmorpgcore.rpgclass.RPGClassManager;
import me.jwhz.mmorpgcore.rpgclass.health.HealthManager;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaManager;
import me.jwhz.mmorpgcore.skills.SkillCardManager;
import me.jwhz.mmorpgcore.totems.TotemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    private MongoDB database;
    private MagicAPI magicAPI;

    public MMORPGCorePlaceholderExpansion mmorpgCorePlaceholderExpansion;

    private List<Manager> managers;

    private SkillCardManager skillCardManager;
    private RPGClassManager rpgClassManager;
    private CommandManager commandManager;
    private HealthManager healthManager;
    private PlayerManager playerManager;
    private TotemManager totemManager;
    private ManaManager manaManager;
    private Messages messages;
    private Config config;

    @Override
    public void onEnable() {

        instance = this;

        this.database = new MongoDB();

        if (Bukkit.getPluginManager().getPlugin("Magic") != null)
            this.magicAPI = (MagicAPI) Bukkit.getPluginManager().getPlugin("Magic");

        managers = new ArrayList<>();

        managers.add((skillCardManager = new SkillCardManager()));
        managers.add((rpgClassManager = new RPGClassManager()));
        managers.add((commandManager = new CommandManager()));
        managers.add((healthManager = new HealthManager()));
        managers.add((playerManager = new PlayerManager()));
        managers.add((totemManager = new TotemManager()));
        managers.add((manaManager = new ManaManager()));

        this.messages = new Messages();
        this.config = new Config(this);

        mmorpgCorePlaceholderExpansion = new MMORPGCorePlaceholderExpansion(this);

        managers.forEach(Manager::onEnable);

    }

    @Override
    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers())
            player.kickPlayer("Reloading core...");

        managers.forEach(Manager::onDisable);

        database.mongoClient.close();

    }

    public void onReload() {

        managers.forEach(Manager::onReload);

        reloadConfig();

    }

    public MongoDB getDatabase() {
        return database;
    }

    public SkillCardManager getSkillCardManager() {
        return skillCardManager;
    }
    public RPGClassManager getRpgClassManager() {
        return rpgClassManager;
    }
    public CommandManager getCommandManager() {
        return commandManager;
    }
    public HealthManager getHealthManager() {
        return healthManager;
    }
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public TotemManager getTotemManager() {
        return totemManager;
    }
    public ManaManager getManaManager() {
        return manaManager;
    }
    public Messages getMessages() {
        return messages;
    }
    public Config getConfigFile() {
        return config;
    }

    public static MMORPGCore getInstance() {

        return instance;

    }

    public MagicAPI getMagicAPI() {
        return magicAPI;
    }
}
