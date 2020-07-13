package me.jwhz.mmorpgcore.totems;

import me.jwhz.mmorpgcore.manager.ManagerObject;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
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
    private double startingAngle;
    public long othersPickupTime;
    private boolean allowPickup;
    private Location center;

    public Totem(TotemConfiguration totemConfiguration, Player player, Item itemEntity) {

        this.totemConfiguration = totemConfiguration;
        this.uuid = player.getUniqueId();
        this.mana = DBPlayer.getPlayer(player).getCurrentProfile().getPlayerStats().getMana();
        this.location = itemEntity.getLocation();
        this.itemEntity = itemEntity;

        this.lastsFor = (int) Math.round((mana / totemConfiguration.getManaPerSecond()) + 0.49);
        this.othersPickupTime = System.currentTimeMillis() + ((lastsFor + totemConfiguration.getPickupDelay()) * 1000);
        this.startingAngle = 0;
        this.allowPickup = false;

        this.itemEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getEntityName().replace("%seconds%", lastsFor + "")));
        this.itemEntity.setCustomNameVisible(true);

        this.center = itemEntity.getLocation();

        DBPlayer.getPlayer(player).getCurrentProfile().getPlayerStats().setMana(0);

    }

    public void tick() {

        if (allowPickup)
            return;

        this.mana -= totemConfiguration.getManaPerSecond();

        if (mana <= 0) {

            mana = 0;

            this.allowPickup = true;

            if (getPlayer().getInventory().firstEmpty() != -1) {

                getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getTotemReturnedMessage()));
                getPlayer().getInventory().addItem(totemConfiguration.getItem());
                getItemEntity().remove();
                setItemEntity(null);

            } else {


                getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getOutOfManaMessage()));

                othersPickupTime = System.currentTimeMillis() + (totemConfiguration.getPickupDelay() * 1000);

                this.itemEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getPickupName()));

            }

            return;

        }

        this.lastsFor--;

        this.itemEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getEntityName().replace("%seconds%", lastsFor + "")));

        if (totemConfiguration.getSize() > 0) {

            startingAngle += Math.PI / 16;

            double radius = totemConfiguration.getRadius();

            for (double angle = startingAngle; angle <= (Math.PI * 2) + startingAngle; angle += Math.PI / (Math.pow(radius, 1.5)))
                itemEntity.getWorld().spawnParticle(
                        Particle.REDSTONE,
                        new Location(center.getWorld(), center.getX() + (radius * Math.cos(angle)), center.getY(), center.getZ() + (radius * Math.sin(angle))),
                        1,
                        new Particle.DustOptions(totemConfiguration.getColor(), 2)
                );

        }

    }

    public void pickup() {

        if (!allowPickup) {

            this.allowPickup = true;

            othersPickupTime = System.currentTimeMillis() + (totemConfiguration.getPickupDelay() * 1000);

            this.itemEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', totemConfiguration.getPickupName()));

        }

    }

    public boolean isWithin(Player player) {

        return player.getLocation().distance(center) <= totemConfiguration.getRadius();

    }

    public boolean canPickup() {

        return allowPickup;

    }

    public double getMana() {

        return mana;

    }

    public long getOthersPickupTime() {

        return othersPickupTime;

    }

    public Item getItemEntity() {

        return itemEntity;

    }

    public void setItemEntity(Item itemEntity) {

        this.itemEntity = itemEntity;

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
