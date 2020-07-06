package me.jwhz.mmorpgcore;

import me.jwhz.mmorpgcore.command.CommandManager;
import me.jwhz.mmorpgcore.config.files.Config;
import me.jwhz.mmorpgcore.config.files.Messages;
import me.jwhz.mmorpgcore.database.MongoDB;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.PlayerManager;
import me.jwhz.mmorpgcore.rpgclass.mana.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    public MongoDB database;

    public MMORPGCorePlaceholderExpansion mmorpgCorePlaceholderExpansion;

    private List<Manager> managers;
    public CommandManager commandManager;
    public PlayerManager playerManager;
    public ManaManager manaManager;
    public Messages messages;
    public Config config;


    @Override
    public void onEnable() {

        instance = this;

        this.database = new MongoDB();
        database = new MongoDB();

        managers = new ArrayList<>();

        managers.add((commandManager = new CommandManager()));
        managers.add((playerManager = new PlayerManager()));
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

    public static MMORPGCore getInstance() {

        return instance;

    }

}
