package me.jwhz.mmorpgcore.command.commands;

import me.jwhz.mmorpgcore.command.CommandBase;
import me.jwhz.mmorpgcore.gui.guis.LevelsGUI;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.rpgclass.passive.Passive;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandBase.Info("stats")
public class StatsCMD extends CommandBase {
    @Override
    public void onCommand(CommandSender sender, String[] args) {

        DBPlayer dbPlayer = DBPlayer.getPlayer((Player) sender);

        if (args[0].equalsIgnoreCase("passives")) {

            dbPlayer.sendMessage("&2&lPassives:");

            for (Passive passive : dbPlayer.getCurrentProfile().getPassives())
                dbPlayer.sendMessage("&a- " + passive.toString());

            return;

        }

        if (args[0].equalsIgnoreCase("levels")) {

            new LevelsGUI(dbPlayer.getPlayer());

            return;

        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
