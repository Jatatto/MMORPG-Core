package me.jwhz.mmorpgcore;

import me.jwhz.mmorpgcore.command.CommandManager;
import me.jwhz.mmorpgcore.database.MongoDB;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    public MongoDB database;

    private List<Manager> managers;
    public CommandManager commandManager;
    public PlayerManager playerManager;
    public Config config;

    @Override
    public void onEnable() {

        instance = this;

        this.database = new MongoDB();

        database = new MongoDB();

        managers = new ArrayList<>();

        managers.add((commandManager = new CommandManager()));
        managers.add((playerManager = new PlayerManager()));

        this.config = new Config(this);

        managers.forEach(Manager::onEnable);

    }

    @Override
    public void onDisable() {

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
