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
import me.jwhz.mmorpgcore.totems.TotemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MMORPGCore extends JavaPlugin {

    private static MMORPGCore instance;

    public MongoDB database;
    public MagicAPI magicAPI;
    public DungeonsXL dungeonsXL;

    public MMORPGCorePlaceholderExpansion mmorpgCorePlaceholderExpansion;

    private List<Manager> managers;

    public RPGClassManager rpgClassManager;
    public CommandManager commandManager;
    public HealthManager healthManager;
    public PlayerManager playerManager;
    public TotemManager totemManager;
    public ManaManager manaManager;

    public Messages messages;
    public Config config;

    @Override
    public void onEnable() {

        instance = this;

        this.database = new MongoDB();

        if (Bukkit.getPluginManager().getPlugin("Magic") != null)
            this.magicAPI = (MagicAPI) Bukkit.getPluginManager().getPlugin("Magic");



        if (Bukkit.getPluginManager().getPlugin("DungeonsXL") != null)
            this.dungeonsXL = (DungeonsXL) Bukkit.getPluginManager().getPlugin("DungeonsXL");

        managers = new ArrayList<>();

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

    public static MMORPGCore getInstance() {

        return instance;

    }

}
