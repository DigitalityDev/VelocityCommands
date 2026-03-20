package dev.digitality.velocitycommands.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import dev.digitality.velocitycommands.managers.CommandRegistry;
import dev.digitality.velocitycommands.managers.ConfigManager;
import dev.digitality.velocitycommands.utils.ChatUtils;

import java.util.List;

public class BlockCommands implements RawCommand {

    private static final String USAGE = ConfigManager.getString("usage-message",
            "<red>Usage: <gray>{usage}").replace("{usage}", "/vcmds <reload|list|disabled|overridden> [args]");

    @Override
    public void execute(Invocation invocation) {
        CommandSource src = invocation.source();
        String[] args = invocation.arguments().isBlank()
                ? new String[0]
                : invocation.arguments().trim().split("\\s+");

        if (!src.hasPermission("velocitycommands.staff")) {
            src.sendMessage(ChatUtils.prefixed("<red>You don't have permission to do that!"));
            return;
        }

        if (args.length == 0) {
            src.sendMessage(ChatUtils.prefixed("<red>You don't have permission to do that!"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(src);
            case "list" -> handleList(src);
            default -> {
                if (args.length < 3) {
                    src.sendMessage(ChatUtils.prefixed(USAGE));
                    return;
                }

                String category = args[0].toLowerCase();
                String action = args[1].toLowerCase();
                String command = args[2].toLowerCase();

                switch (category) {
                    case "disabled" -> handleDisabled(src, action, command);
                    case "overridden" -> handleOverridden(src, action, command, args);
                    default -> src.sendMessage(ChatUtils.prefixed(USAGE));
                }
            }
        }
    }

    private void handleReload(CommandSource src) {
        ConfigManager.reload();
        CommandRegistry.reload();
        src.sendMessage(ChatUtils.prefixed(
                ConfigManager.getString("reload-message", "<green>Configuration reloaded successfully!")));
    }

    private void handleList(CommandSource src) {
        src.sendMessage(ChatUtils.prefixed(
                "<yellow>Disabled: <gray>" + String.join(", ", CommandRegistry.getDisabledCommands())));
        src.sendMessage(ChatUtils.prefixed(
                "<yellow>Overridden: <gray>" + String.join(", ", CommandRegistry.getOverriddenCommands().keySet())));
        src.sendMessage(ChatUtils.prefixed(
                "<yellow>Tab-complete blocked: <gray>" + String.join(", ", CommandRegistry.getDisabledTabComplete())));
    }

    private void handleDisabled(CommandSource src, String action, String command) {
        switch (action) {
            case "add" -> {
                if (CommandRegistry.isDisabled(command)) {
                    src.sendMessage(ChatUtils.prefixed(
                            "<yellow>Command <gray>/" + command + " <yellow>is already disabled."));
                    return;
                }
                CommandRegistry.addDisabled(command);
                src.sendMessage(ChatUtils.prefixed(
                        "<green>Disabled command <gray>/" + command));
            }
            case "remove" -> {
                if (!CommandRegistry.isDisabled(command)) {
                    src.sendMessage(ChatUtils.prefixed(
                            "<yellow>Command <gray>/" + command + " <yellow>is not disabled."));
                    return;
                }
                CommandRegistry.removeDisabled(command);
                src.sendMessage(ChatUtils.prefixed(
                        "<red>Re-enabled command <gray>/" + command));
            }
            default -> src.sendMessage(ChatUtils.prefixed(USAGE));
        }
    }

    private void handleOverridden(CommandSource src, String action, String command, String[] args) {
        switch (action) {
            case "add" -> {
                if (args.length < 4) {
                    src.sendMessage(ChatUtils.prefixed(
                            "<white>Usage: <aqua>/vcmds overridden add <command> <message>"));
                    return;
                }
                String message = String.join(" ", List.of(args).subList(3, args.length));
                CommandRegistry.addOverridden(command, message);
                src.sendMessage(ChatUtils.prefixed(
                        "<green>Overridden command <gray>/" + command));
            }
            case "remove" -> {
                if (!CommandRegistry.isOverridden(command)) {
                    src.sendMessage(ChatUtils.prefixed(
                            "<yellow>Command <gray>/" + command + " <yellow>is not overridden."));
                    return;
                }
                CommandRegistry.removeOverridden(command);
                src.sendMessage(ChatUtils.prefixed(
                        "<red>Removed override for <gray>/" + command));
            }
            default -> src.sendMessage(ChatUtils.prefixed(USAGE));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments().isBlank()
                ? new String[0]
                : invocation.arguments().trim().split("\\s+");

        // /vcmds <tab>
        if (args.length == 0 || (args.length == 1 && !invocation.arguments().endsWith(" ")))
            return List.of("disabled", "overridden", "reload", "list");

        // /vcmds disabled <tab>
        if (args.length == 1 && invocation.arguments().endsWith(" "))
            return List.of("disabled", "overridden", "reload", "list");

        // /vcmds disabled|overridden <tab>
        if (args.length == 2 && !invocation.arguments().endsWith(" "))
            return List.of("add", "remove");

        if (args.length == 2 && invocation.arguments().endsWith(" "))
            return List.of("add", "remove");

        return List.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("velocitycommands.staff");
    }
}