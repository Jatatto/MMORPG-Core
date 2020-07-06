package me.jwhz.mmorpgcore.rpgclass.mana;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.profiledata.PlayerStats;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ManaManager extends Manager {

    public ManaManager() {

        super("config");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {

            PlayerStats playerStats;
            ManaSettings manaSettings;

            for (Player player : Bukkit.getOnlinePlayers()) {

                DBPlayer dbPlayer = DBPlayer.getPlayer(player);

                if (dbPlayer != null && dbPlayer.getCurrentProfile() != null) {

                    playerStats = dbPlayer.getCurrentProfile().getPlayerStats();
                    manaSettings = dbPlayer.getCurrentProfile().getProfileSettings().getManaSettings();

                    playerStats.setMana(Math.min(playerStats.getMana() + manaSettings.getManaRegeneration(), manaSettings.getMaxMana()));

                    PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(
                            PlaceholderAPI.setPlaceholders(player, core.messages.manaBar)
                    ), ChatMessageType.GAME_INFO);

                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                }

            }

        }, 0, 20);

    }

}
