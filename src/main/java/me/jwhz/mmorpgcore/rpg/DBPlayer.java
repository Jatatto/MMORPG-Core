package me.jwhz.mmorpgcore.rpg;

import com.mongodb.DBObject;
import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.manager.ManagerObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DBPlayer extends ManagerObject<UUID> {

    private UUID id;
    private String name;
    private DBObject profile;

    DBPlayer(Player player) {

        this.id = player.getUniqueId();
        this.name = player.getName();

    }

    public Player getPlayer() {

        return Bukkit.getPlayer(id);

    }

    public void loadData(DBObject dbObject) {

        this.profile = dbObject;

    }

    public DBObject getData() {

        return profile;

    }

    @Override
    public UUID getIdentifier() {

        return id;

    }

    public static DBPlayer getPlayer(Player player) {

        return getPlayer(player.getUniqueId());

    }

    public static DBPlayer getPlayer(UUID id) {

        return MMORPGCore.getInstance().playerManager.getList().stream().filter(dbPlayer -> dbPlayer.id.equals(id)).findFirst().orElse(null);

    }

}
