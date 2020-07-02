package me.jwhz.mmorpgcore.profile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Profile {

    private Document data;

    public Profile(Document data) {

        this.data = data;

    }

    public String getProfileName() {

        return String.valueOf(data.get("profile name"));

    }

    public UUID getProfileUUID() {

        return UUID.fromString(data.getString("profile uuid"));

    }

    public UUID getOwner() {

        return UUID.fromString(data.getString("owner"));

    }

    public void loadProfile() {

        Player player = Bukkit.getPlayer(getOwner());

        if (data.containsKey("player stats")) {

            Document playerStats = (Document) data.get("player stats");

            player.teleport(playerStats.containsKey("location") ? BukkitSerialization.locationFromString(playerStats.getString("location")) : MMORPGCore.getInstance().config.getNewProfileSpawn());

            player.setMaxHealth(playerStats.get("max health", 20.0));
            player.setHealth(playerStats.get("health", 20.0));

            player.setFoodLevel(playerStats.get("food", 20));

            if (playerStats.containsKey("xp")) {

                Document xp = (Document) playerStats.get("xp");

                player.setLevel(xp.getInteger("xp level"));
                player.setExp(Float.valueOf(xp.getString("current xp")));

            } else {

                player.setLevel(0);
                player.setExp(0);

            }

            player.getEnderChest().clear();

            if (playerStats.containsKey("enderchest"))
                try {
                    player.getEnderChest().setContents(BukkitSerialization.itemStackArrayFromBase64(playerStats.getString("enderchest")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);

            if (playerStats.containsKey("inventory")) {

                try {

                    player.getInventory().setContents(BukkitSerialization.itemStackArrayFromBase64(playerStats.getString("inventory")));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (playerStats.containsKey("armor")) {

                try {

                    player.getInventory().setArmorContents(BukkitSerialization.itemStackArrayFromBase64(playerStats.getString("armor")));

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            if (playerStats.containsKey("potions"))

                for (Document object : (List<Document>) playerStats.get("potions")) {

                    player.addPotionEffect(new PotionEffect(
                            PotionEffectType.getByName(object.getString("type")),
                            object.getInteger("duration"),
                            object.getInteger("amplifier"),
                            object.getBoolean("ambient"),
                            object.getBoolean("has particles"),
                            object.getBoolean("has icon")
                    ));

                }

        } else
            player.teleport(MMORPGCore.getInstance().config.getNewProfileSpawn());

    }

    public void unloadProfile() {

        save();

        Player player = Bukkit.getPlayer(getOwner());

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.setHealth(20);
        player.setMaxHealth(20);
        player.setFoodLevel(20);
        player.setTotalExperience(0);
        player.setLevel(0);

        Iterator<PotionEffect> potions = player.getActivePotionEffects().iterator();

        while (potions.hasNext()) {

            player.removePotionEffect(potions.next().getType());

            potions.remove();

        }

        player.getEnderChest().clear();

    }

    public void save() {

        Player player = Bukkit.getPlayer(getOwner());

        Document playerStats = new Document();

        playerStats.put("enderchest", BukkitSerialization.itemStackArrayToBase64(player.getEnderChest().getContents()));
        playerStats.put("inventory", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
        playerStats.put("armor", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getArmorContents()));
        playerStats.put("health", player.getHealth());
        playerStats.put("max health", player.getMaxHealth());
        playerStats.put("food", player.getFoodLevel());
        playerStats.put("location", BukkitSerialization.locationToString(player.getLocation()));

        Document xp = new Document();
        xp.put("xp level", player.getLevel());
        xp.put("current xp", player.getExp() + "");

        playerStats.put("xp", xp);

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

            playerStats.put("potions", potions);

        } else
            playerStats.remove("potions");

        data.put("player stats", playerStats);

    }

    public Document getData() {

        return data;

    }

}
