package me.jwhz.mmorpgcore.rpg;

import com.mongodb.BasicDBObject;
import me.jwhz.mmorpgcore.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerManager extends Manager<DBPlayer> implements Listener {

    public PlayerManager() {

        super("config");

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    @Override
    public void onEnable() {

        for (Player player : Bukkit.getOnlinePlayers())
            loadPlayer(player);

    }

    @Override
    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers())
            unloadPlayer(player);

    }

    public void loadPlayer(Player player) {

        DBPlayer dbPlayer = new DBPlayer(player);

        dbPlayer.loadData(
                core.database.isRegistered(player.getUniqueId()) ?
                        core.database.getPlayer(player.getUniqueId()) :
                        new BasicDBObject("uuid", player.getUniqueId())
        );

        getList().add(new DBPlayer(player));

    }

    public void unloadPlayer(Player player) {

        DBPlayer dbPlayer = getList().stream().filter(db -> db.getPlayer().equals(player)).findFirst().orElse(null);

        core.database.collection.insert(dbPlayer.getData());

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        loadPlayer(e.getPlayer());

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        unloadPlayer(e.getPlayer());

    }

}
