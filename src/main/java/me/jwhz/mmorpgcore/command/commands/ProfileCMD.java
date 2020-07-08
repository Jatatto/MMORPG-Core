package me.jwhz.mmorpgcore.command.commands;

import me.jwhz.mmorpgcore.command.CommandBase;
import me.jwhz.mmorpgcore.gui.guis.ProfileSelectGUI;
import me.jwhz.mmorpgcore.gui.guis.SelectClassGUI;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandBase.Info("profile")
public class ProfileCMD extends CommandBase {

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        DBPlayer player = DBPlayer.getPlayer(((Player) sender).getUniqueId());

        if (args.length == 0) {

            new ProfileSelectGUI(player.getPlayer());

            // player.sendMessage("&aCurrent profile: " + player.getCurrentProfile().getProfileName());
            return;

        }

        if (args[0].equalsIgnoreCase("class")) {

            if (player.getCurrentProfile().getRPGClass() == null)
                new SelectClassGUI(player.getPlayer());
            else
                player.sendMessage("&aCurrent class: " + player.getCurrentProfile().getRPGClass().getClassName());

            return;

        }

        if (args[0].equalsIgnoreCase("list")) {

            player.sendMessage("&b&nProfiles: ");

            for (Profile profile : player.getProfiles())
                player.sendMessage("&7- &b" + profile.getProfileName());

            return;

        }

        if (args[0].equalsIgnoreCase("create") && args.length > 1) {

            String name = args[1];

            if (player.isProfile(name)) {

                player.sendMessage("&cThis profile already exists.");
                return;

            }

            player.createProfile(name);
            player.sendMessage("&cProfile " + name + " created.");

        }

        if (player.isProfile(args[0])) {

            player.setCurrentProfile(player.getProfile(args[0]));
            player.sendMessage("&aYou changed your current profile to " + player.getCurrentProfile().getProfileName() + ".");

        }


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        DBPlayer player = DBPlayer.getPlayer(((Player) sender).getUniqueId());

        if (args.length == 0) {

            List<String> complete = new ArrayList<>();
            complete.add("create");
            complete.add("list");

            for (Profile profile : player.getProfiles())
                complete.add(profile.getProfileName());

            return complete;

        }

        return null;

    }

}
