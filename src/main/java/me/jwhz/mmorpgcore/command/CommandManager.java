package me.jwhz.mmorpgcore.command;

import me.jwhz.mmorpgcore.command.commands.ProfileCMD;
import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.manager.Manager;
import org.bukkit.command.PluginCommand;


public class CommandManager extends Manager<CommandBase> {

    public CommandManager() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        getList().add(new ProfileCMD());

        for (CommandBase commandInfo : getList()) {

            PluginCommand command = commandInfo.core.getCommand(commandInfo.getAnnotationInfo().value());

            command.setExecutor(commandInfo);
            command.setTabCompleter(commandInfo);
            command.setPermission(commandInfo.getAnnotationInfo().permission());
            command.setPermissionMessage(commandInfo.noPermission);

            ConfigHandler.setPresets(commandInfo, getFile());
            ConfigHandler.reload(commandInfo, getFile());

        }

    }

    @Override
    public void onReload() {

        for (CommandBase command : getList()) {

            ConfigHandler.setPresets(command, getFile());
            ConfigHandler.reload(command, getFile());

        }

    }

}
