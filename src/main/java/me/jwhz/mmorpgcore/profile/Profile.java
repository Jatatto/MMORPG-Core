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

        player.teleport(data.containsKey("location") ? BukkitSerialization.locationFromString(data.getString("location")) : MMORPGCore.getInstance().config.getNewProfileSpawn());

        player.setMaxHealth(data.get("max health", 20.0));
        player.setHealth(data.get("health", 20.0));

        player.setFoodLevel(data.get("food", 20));

        player.setTotalExperience(data.get("total exp", 0));
        player.setExp(Float.valueOf(data.get("exp", "0")));

        player.getEnderChest().clear();

        if (data.containsKey("enderchest"))
            try {
                player.getEnderChest().setContents(BukkitSerialization.itemStackArrayFromBase64(data.getString("enderchest")));
            } catch (IOException e) {
                e.printStackTrace();
            }

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        if (data.containsKey("inventory")) {

            try {

                player.getInventory().setContents(BukkitSerialization.itemStackArrayFromBase64(data.getString("inventory")));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (data.containsKey("armor")) {

            try {

                player.getInventory().setArmorContents(BukkitSerialization.itemStackArrayFromBase64(data.getString("armor")));

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        if (data.containsKey("potions"))

            for (Document object : (List<Document>) data.get("potions")) {

                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.getByName(object.getString("type")),
                        object.getInteger("duration"),
                        object.getInteger("amplifier"),
                        object.getBoolean("ambient"),
                        object.getBoolean("has particles"),
                        object.getBoolean("has icon")
                ));

            }


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
        player.setExp(0f);

        Iterator<PotionEffect> potions = player.getActivePotionEffects().iterator();

        while (potions.hasNext()) {

            player.removePotionEffect(potions.next().getType());

            potions.remove();

        }

        player.getEnderChest().clear();

    }

    public void save() {

        Player player = Bukkit.getPlayer(getOwner());

        data.put("inventory", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
        data.put("armor", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getArmorContents()));
        data.put("health", player.getHealth());
        data.put("max health", player.getMaxHealth());
        data.put("food", player.getFoodLevel());
        data.put("location", BukkitSerialization.locationToString(player.getLocation()));
        data.put("total exp", player.getTotalExperience());
        data.put("exp", player.getExp() + "");

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

            data.put("potions", potions);

        } else
            data.remove("potions");

        data.put("enderchest", BukkitSerialization.itemStackArrayToBase64(player.getEnderChest().getContents()));


    }

    public Document getData() {

        return data;

    }

}
