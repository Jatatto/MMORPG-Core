package me.jwhz.mmorpgcore.profile;

import me.jwhz.mmorpgcore.manager.Manager;
import org.bson.Document;
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

    public int getMaximumProfiles(Player player) {

        int profileCap = 1;

        if (player.isOp() || player.hasPermission(""))
            return Integer.MAX_VALUE;

        for (int i = 2; i < 10; i++)
            if (player.hasPermission("MMORPGCore.user.profiles-limit." + i))
                profileCap = i;

        return profileCap;

    }

    public void loadPlayer(Player player) {

        DBPlayer dbPlayer = new DBPlayer(player);

        Document document = core.database.getPlayer(player.getUniqueId());

        if (document == null)
            document = new Document("uuid", player.getUniqueId().toString());

        dbPlayer.loadData(
                document
        );

        getList().add(dbPlayer);

        if (dbPlayer.getProfiles().size() == 0) {

            dbPlayer.createProfile("default");

            dbPlayer.setCurrentProfile(dbPlayer.getProfile("default"));

        } else if (dbPlayer.getLastPlayed() != null)
            dbPlayer.setCurrentProfile(dbPlayer.getLastPlayed());

        dbPlayer.sendMessage(core.messages.profileLoaded.replace("%profile%", dbPlayer.getCurrentProfile().getProfileName()));

    }

    public void unloadPlayer(Player player) {

        DBPlayer dbPlayer = DBPlayer.getPlayer(player);

        if (dbPlayer.getCurrentProfile() != null)
            dbPlayer.getCurrentProfile().unloadProfile();

        dbPlayer.save();

        getList().remove(dbPlayer);

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
