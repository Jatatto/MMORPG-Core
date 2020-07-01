package me.jwhz.mmorpgcore.command;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.manager.Manager;
import org.bukkit.command.PluginCommand;


public class CommandManager extends Manager<CommandBase> {

    public CommandManager() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        for(CommandBase commandInfo : getList()){

            PluginCommand command = commandInfo.core.getCommand(commandInfo.getAnnotationInfo().command());

            command.setExecutor(commandInfo);
            command.setTabCompleter(commandInfo);
            command.setPermission(commandInfo.getAnnotationInfo().permission());
            command.setPermissionMessage(commandInfo.noPermission);

            ConfigHandler.setPresets(commandInfo, getFile());
            ConfigHandler.reload(commandInfo, getFile());

            list.add(commandInfo);

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
