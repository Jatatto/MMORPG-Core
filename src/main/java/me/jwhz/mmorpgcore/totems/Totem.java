package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.manager.ManagerObject;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Totem extends ManagerObject<Location> {

    private TotemConfiguration totemConfiguration;
    private UUID uuid;
    private double mana;
    private Location location;
    private Item itemEntity;

    private int lastsFor;
    private long othersPickupTime;
    private boolean shownMessage = false;

    public Totem(TotemConfiguration totemConfiguration, Player player, Item itemEntity) {

        this.totemConfiguration = totemConfiguration;
        this.uuid = player.getUniqueId();
        this.mana = DBPlayer.getPlayer(player).getCurrentProfile().getPlayerStats().getMana();
        this.location = itemEntity.getLocation();
        this.itemEntity = itemEntity;

        this.lastsFor = (int) Math.round((mana / totemConfiguration.getManaPerSecond()) + 0.5);
        this.othersPickupTime = System.currentTimeMillis() + ((lastsFor + totemConfiguration.getPickupDelay()) * 1000);

    }

    public void tick() {

        this.mana -= totemConfiguration.getManaPerSecond();

        if (mana <= 0) {

            mana = 0;

            if (!shownMessage) {

                this.shownMessage = true;
                getPlayer().sendMessage(totemConfiguration.getOutOfManaMessage());

            }

            return;

        }

    }

    public double getMana() {

        return mana;

    }

    public TotemConfiguration getTotemConfiguration() {

        return totemConfiguration;

    }

    public Player getPlayer() {

        return Bukkit.getPlayer(uuid);

    }

    @Override
    public Location getIdentifier() {

        return location;

    }

}
