package dev.digitality.velocitycommands.commands;

import com.velocitypowered.api.command.SimpleCommand;
import dev.digitality.velocitycommands.managers.ConfigManager;
import dev.digitality.velocitycommands.utils.ChatUtils;


public class VelocityCommands implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (!invocation.source().hasPermission("velocitycommands.staff")) {
            invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize(ConfigManager.getConfig().getString("no-permission"))));
            return;
        }

        if (!(invocation.arguments().length == 3) && !(invocation.arguments().length == 4)) {
            invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
            return;
        }
        if (invocation.arguments()[0].equals("overriden")) {
            if (invocation.arguments()[1].equals("add")) {
                if (invocation.arguments().length == 4) {
                    ConfigManager.getConfig().set(invocation.arguments()[2], invocation.arguments()[3]);
                    ConfigManager.saveConfig(ConfigManager.getConfig(), "settings.yml");
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<green>Added overriden command <gray>/" + invocation.arguments()[2])));
                } else {
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
                }
            } else if (invocation.arguments()[1].equals("remove")) {
                if (invocation.arguments().length == 3) {
                    ConfigManager.getConfig().set(invocation.arguments()[2], null);
                    ConfigManager.saveConfig(ConfigManager.getConfig(), "settings.yml");
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<red>Removed overriden command <gray>/" + invocation.arguments()[2])));
                } else {
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
                }
            } else {
                invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
            }
        } else if (invocation.arguments()[0].equals("disabled")) {
            if (invocation.arguments()[1].equals("add")) {
                if (invocation.arguments().length == 3) {
                    ConfigManager.getConfig().set("disabled-commands", ConfigManager.getConfig().getStringList("disabled-commands").add(invocation.arguments()[2]));
                    ConfigManager.saveConfig(ConfigManager.getConfig(), "settings.yml");
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<green>Added disabled command <gray>/" + invocation.arguments()[2])));
                } else {
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
                }
            } else if (invocation.arguments()[1].equals("remove")) {
                if (invocation.arguments().length == 3) {
                    ConfigManager.getConfig().set("disabled-commands", ConfigManager.getConfig().getStringList("disabled-commands").remove(invocation.arguments()[2]));
                    ConfigManager.saveConfig(ConfigManager.getConfig(), "settings.yml");
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<red>Removed disabled command <gray>/" + invocation.arguments()[2])));
                } else {
                    invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
                }
            } else {
                invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
            }
        } else {
            invocation.source().sendMessage(ChatUtils.getPrefix().append(ChatUtils.colorize("<white>Usage: <aqua>/vcmds <overriden/disabled> <add/remove> <command>")));
        }

    }
}
