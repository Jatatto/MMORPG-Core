package me.jwhz.mmorpgcore;

import me.jwhz.mmorpgcore.command.CommandManager;
import me.jwhz.mmorpgcore.database.IDatabase;
import me.jwhz.mmorpgcore.database.mysql.MongoDatabase;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.rpg.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    public MongoDatabase database;

    private List<Manager> managers;
    public CommandManager commandManager;
    public PlayerManager playerManager;

    @Override
    public void onEnable() {

        instance = this;

        this.database = new MongoDatabase();

        database = new MongoDatabase();

        managers = new ArrayList<>();

        managers.add((commandManager = new CommandManager()));
        managers.add((playerManager = new PlayerManager()));

        managers.forEach(Manager::onEnable);

    }

    @Override
    public void onDisable() {

        managers.forEach(Manager::onDisable);

    }

    public void onReload() {

        managers.forEach(Manager::onReload);

    }

    public static MMORPGCore getInstance() {

        return instance;

    }

}
