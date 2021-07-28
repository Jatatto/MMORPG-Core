package me.jwhz.mmorpgcore.command.commands;

import me.jwhz.mmorpgcore.command.CommandBase;
import me.jwhz.mmorpgcore.config.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandBase.Info("totem")
public class TotemCMD extends CommandBase {

    @ConfigValue("messages.totems.totem help page")
    List<String> totems = Arrays.asList(
            "&aTotem help:",
            "&7- &a/totem give <player> <totem>"
    );
    @ConfigValue("messages.totems.player not found")
    public String playerNotFound = "&cPlayer not found.";

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {

            for (String line : totems)
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));

            return;

        }

        if (args[0].equalsIgnoreCase("give") && args.length == 3) {

            Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {

                sender.sendMessage(playerNotFound);
                return;

            }

            if (!core.getTotemManager().isTotemConfiguration(args[2])) {

                sender.sendMessage("&cCan't find totem configuration.");
                return;

            }

            player.getInventory().addItem(core.getTotemManager().getTotemConfiguration(args[2]).getItem());

            return;

        }

        for (String line : totems)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
