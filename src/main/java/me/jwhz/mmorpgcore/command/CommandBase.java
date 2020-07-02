package me.jwhz.mmorpgcore.command;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.manager.ManagerObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CommandBase extends ManagerObject<String> implements CommandExecutor, TabCompleter {

    @ConfigValue("Messages.no permission")
    String noPermission = "&cYou do not have permission to use this command!";

    private Info annotationInfo = getClass().getAnnotation(Info.class);
    protected MMORPGCore core = MMORPGCore.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (annotationInfo.value().equalsIgnoreCase(command.getName())) {

            if (annotationInfo.onlyPlayers() && !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use this command!"));
                return true;
            }

            onCommand(sender, args);

        }

        return true;

    }

    @Override
    public String getIdentifier() {

        return annotationInfo.value();

    }

    public Info getAnnotationInfo() {

        return annotationInfo;

    }

    public abstract void onCommand(CommandSender sender, String[] args);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {

        String value();

        String permission() default "MMORPGCore.user";

        boolean onlyPlayers() default true;

    }

}
