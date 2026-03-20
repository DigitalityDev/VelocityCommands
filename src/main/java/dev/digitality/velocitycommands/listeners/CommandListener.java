package dev.digitality.velocitycommands.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.Player;
import dev.digitality.velocitycommands.managers.CommandRegistry;
import dev.digitality.velocitycommands.managers.ConfigManager;
import dev.digitality.velocitycommands.utils.ChatUtils;

public class CommandListener {

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player player)) return;
        if (player.hasPermission("velocitycommands.bypass")) return;

        String command = resolveCommand(event.getCommand());

        if (CommandRegistry.isDisabled(command)) {
            player.sendMessage(ChatUtils.colorize(ConfigManager.getString("default-message", "Unknown command.")));
            event.setResult(CommandExecuteEvent.CommandResult.denied());
            return;
        }

        if (CommandRegistry.isOverridden(command)) {
            player.sendMessage(CommandRegistry.getOverrideMessage(command));
            event.setResult(CommandExecuteEvent.CommandResult.denied());
        }
    }

    @Subscribe
    public void onTabComplete(PlayerAvailableCommandsEvent event) {
        if (event.getPlayer().hasPermission("velocitycommands.bypass")) return;

        event.getRootNode().getChildren().removeIf(node ->
                CommandRegistry.isTabCompleteDisabled(node.getName())
                        || CommandRegistry.isDisabled(node.getName())
        );
    }

    private String resolveCommand(String rawCommand) {
        String cmd = rawCommand.toLowerCase().split(" ")[0];
        if (cmd.contains(":") && ConfigManager.getBoolean("disable-colon-bypass", true)) {
            cmd = cmd.split(":")[1];
        }
        return cmd;
    }
}