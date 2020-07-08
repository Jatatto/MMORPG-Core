package me.jwhz.mmorpgcore.profile.profiledata;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PlayerStats extends ProfileData {

    public PlayerStats(Profile profile, Document document) {

        super(profile, document, "player stats");

    }

    public double getHealth() {

        return document.get("health", 20.0);

    }

    public double getMaxHealth() {

        return document.get("max health", 20.0);

    }

    public void setMaxHealth(double maxHealth) {

        document.put("max health", maxHealth);

    }

    public double getHealthScale() {

        return getMaxHealth() / 20.0;

    }

    public void setHealth(double health) {

        Player player = Bukkit.getPlayer(profile.getOwner());

        if (player != null) {

            Objects.requireNonNull(Bukkit.getPlayer(profile.getOwner())).setHealth(Math.min(player.getMaxHealth(), health));
            document.put("health", Math.min(DBPlayer.getPlayer(player).getCurrentProfile().getPlayerStats().getMaxHealth(), health * getHealthScale()));
            MMORPGCore.getInstance().manaManager.updateBar(player);

        }

    }

    public Document getExp() {

        return (Document) document.get("xp");

    }

    public List<Document> getPotions() {

        return (List<Document>) document.get("potions");

    }


    public ItemStack[] getEnderChest() {

        try {

            return BukkitSerialization.itemStackArrayFromBase64(document.getString("enderchest"));

        } catch (Exception ignored) {
        }

        return null;

    }

    public ItemStack[] getInventory() {

        try {

            return BukkitSerialization.itemStackArrayFromBase64(document.getString("inventory"));

        } catch (Exception ignored) {
        }

        return null;

    }

    public double getMana() {

        return document.get("mana", profile.getRPGClass() != null ? profile.getRPGClass().getManaSettings().getMaxMana() : 100);

    }

    public void setMana(double mana) {

        document.put("mana", mana);

    }

    public Location getLocation() {

        return BukkitSerialization.locationFromString(document.getString("location"));

    }

    @Override
    public void load(Player player) {

        player.teleport(document.containsKey("location") ? getLocation() : MMORPGCore.getInstance().config.getNewProfileSpawn());

        player.setMaxHealth(20.0);

        document.put("max health", profile.getRPGClass() != null ? profile.getRPGClass().getMaxHealth() : 20.0);
        player.setHealth(document.get("health", 20.0) / getHealthScale());

        player.setFoodLevel(document.get("food", 20));

        if (document.containsKey("xp")) {

            Document xp = getExp();

            player.setLevel(xp.getInteger("xp level"));
            player.setExp(Float.valueOf(xp.getString("current xp")));

        } else {

            player.setLevel(0);
            player.setTotalExperience(0);

        }

        player.getEnderChest().clear();

        if (document.containsKey("enderchest"))
            player.getEnderChest().setContents(getEnderChest());

        player.getInventory().clear();

        if (document.containsKey("inventory"))
            player.getInventory().setContents(getInventory());

        if (document.containsKey("potions"))
            for (Document object : getPotions())
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.getByName(object.getString("type")),
                        object.getInteger("duration"),
                        object.getInteger("amplifier"),
                        object.getBoolean("ambient"),
                        object.getBoolean("has particles"),
                        object.getBoolean("has icon")
                ));

    }

    @Override
    public void unload(Player player) {

        player.getInventory().clear();
        player.setHealth(20);
        player.setMaxHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setTotalExperience(0);

        Iterator<PotionEffect> potions = player.getActivePotionEffects().iterator();

        while (potions.hasNext()) {

            player.removePotionEffect(potions.next().getType());

            potions.remove();

        }

        player.getEnderChest().clear();

    }

    @Override
    public void save(Player player) {

        document.put("enderchest", BukkitSerialization.itemStackArrayToBase64(player.getEnderChest().getContents()));
        document.put("inventory", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
        document.put("health", player.getHealth());

        if (document.containsKey("max health") && profile.getRPGClass() != null)
            document.put("max health", profile.getRPGClass().getMaxHealth());

        document.put("food", player.getFoodLevel());
        document.put("location", BukkitSerialization.locationToString(player.getLocation()));

        Document xp = new Document();
        xp.put("xp level", player.getLevel());
        xp.put("current xp", player.getExp() + "");

        document.put("xp", xp);

        if (player.getActivePotionEffects().size() > 0) {

            List<Document> potions = new ArrayList<>();

            for (PotionEffect effect : player.getActivePotionEffects()) {

                Document obj = new Document();

                obj.put("duration", effect.getDuration());
                obj.put("type", effect.getType().getName());
                obj.put("amplifier", effect.getAmplifier());
                obj.put("has particles", effect.hasParticles());
                obj.put("has icon", effect.hasIcon());
                obj.put("ambient", effect.isAmbient());

                potions.add(obj);

            }

            document.put("potions", potions);

        } else
            document.remove("potions");

        super.save();

    }

}
