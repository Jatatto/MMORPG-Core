package me.jwhz.mmorpgcore.rpgclass.mana;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jwhz.mmorpgcore.events.ManaRegenerationEvent;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.profiledata.PlayerStats;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
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

            RPGClass rpgClass;
            ManaSettings manaSettings;

            for (Player player : Bukkit.getOnlinePlayers()) {

                DBPlayer dbPlayer = DBPlayer.getPlayer(player);

                if (dbPlayer != null && dbPlayer.getCurrentProfile() != null) {

                    rpgClass = dbPlayer.getCurrentProfile().getRPGClass();
                    manaSettings = rpgClass == null ? new ManaSettings(100, 5, "Mana") : rpgClass.getManaSettings();

                    ManaRegenerationEvent manaRegenerationEvent = new ManaRegenerationEvent(dbPlayer, manaSettings.getManaRegeneration());

                    Bukkit.getPluginManager().callEvent(manaRegenerationEvent);

                    if (!manaRegenerationEvent.isCancelled())
                        dbPlayer.getCurrentProfile().getPlayerStats()
                                .setMana(Math.min(dbPlayer.getCurrentProfile().getPlayerStats().getMana() + manaRegenerationEvent.getRegenerationAmount(), manaSettings.getMaxMana()));

                    updateBar(player);

                }

            }

            core.getTotemManager().run();

        }, 0, 20);

    }

    public void updateBar(Player player) {

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(
                PlaceholderAPI.setPlaceholders(player, core.getMessages().manaBar)
        ), ChatMessageType.GAME_INFO));

    }

}
