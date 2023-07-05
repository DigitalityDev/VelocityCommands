package dev.digitality.velocitycommands.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.digitality.velocitycommands.managers.ConfigManager;
import dev.digitality.velocitycommands.utils.ChatUtils;

public class ReloadCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!source.hasPermission("velocitycommands.reload")) {
            source.sendMessage(ChatUtils.colorize("&cYou do not have permission to use this command."));
        } else {
            ConfigManager.reloadConfigs();
            source.sendMessage(ChatUtils.colorize("&aSuccessfully reloaded the config files."));
        }
    }
}
