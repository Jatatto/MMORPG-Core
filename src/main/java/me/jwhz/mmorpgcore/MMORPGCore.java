package me.jwhz.mmorpgcore;

import me.jwhz.mmorpgcore.command.CommandManager;
import me.jwhz.mmorpgcore.database.IDatabase;
import me.jwhz.mmorpgcore.database.mysql.MySQLDatabase;
import me.jwhz.mmorpgcore.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

//
public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    public IDatabase database;

    private List<Manager> managers;
    public CommandManager commandManager;

    @Override
    public void onEnable() {

        instance = this;

        this.database = new MySQLDatabase();

        ((MySQLDatabase) database).openConnection();

        managers = new ArrayList<>();

        managers.add((commandManager = new CommandManager()));

        managers.forEach(Manager::onEnable);

    }

    @Override
    public void onDisable() {

        managers.forEach(Manager::onDisable);

        ((MySQLDatabase) database).closeConnection();

    }

    public void onReload() {

        managers.forEach(Manager::onReload);

    }

    public static MMORPGCore getInstance() {

        return instance;

    }

}
