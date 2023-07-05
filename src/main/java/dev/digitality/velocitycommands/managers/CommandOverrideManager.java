package dev.digitality.velocitycommands.managers;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import dev.digitality.velocitycommands.utils.ChatUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandOverrideManager {
    @Getter
    private static final HashMap<String, Component> overridenCommands = new HashMap<>();
    @Getter
    private static final List<String> disabledCommands = new ArrayList<>();

    @Subscribe
    public void onCommand(CommandExecuteEvent e) {
        if (!(e.getCommandSource() instanceof Player player))
            return;

        String command = e.getCommand().toLowerCase().split(" ")[0];

        if (disabledCommands.contains(command)) {
            player.sendMessage(ChatUtils.colorize("Unknown command. Type \"/help\" for help."));

            e.setResult(CommandExecuteEvent.CommandResult.denied());
        } else if (overridenCommands.containsKey(command)) {
            player.sendMessage(overridenCommands.get(command));

            e.setResult(CommandExecuteEvent.CommandResult.denied());
        }
    }
}