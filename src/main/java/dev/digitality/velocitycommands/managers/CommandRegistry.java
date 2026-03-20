package dev.digitality.velocitycommands.managers;

import dev.digitality.velocitycommands.utils.ChatUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

public class CommandRegistry {

    @Getter
    private static CommandRegistry instance;

    private final Map<String, Component> overriddenCommands = new HashMap<>();
    private final Set<String> disabledCommands = new HashSet<>();
    private final Set<String> disabledTabComplete = new HashSet<>();

    public CommandRegistry() {
        instance = this;
    }

    public static void reload() {
        instance.overriddenCommands.clear();
        instance.disabledCommands.clear();
        instance.disabledTabComplete.clear();

        ConfigManager.getStringList("disabled-commands")
                .forEach(cmd -> instance.disabledCommands.add(cmd.toLowerCase()));

        ConfigManager.getStringList("disable-tab-complete")
                .forEach(cmd -> instance.disabledTabComplete.add(cmd.toLowerCase()));

        Map<String, String> overridden = ConfigManager.getStringMap("overridden-commands");
        if (!overridden.isEmpty()) {
            overridden.forEach((cmd, msg) -> instance.overriddenCommands.put(cmd.toLowerCase(), ChatUtils.colorize(msg)));
        }
    }

    public static boolean isDisabled(String command) {
        return instance.disabledCommands.contains(command.toLowerCase());
    }

    public static void addDisabled(String command) {
        instance.disabledCommands.add(command.toLowerCase());
        persistDisabled();
    }

    public static void removeDisabled(String command) {
        instance.disabledCommands.remove(command.toLowerCase());
        persistDisabled();
    }

    private static void persistDisabled() {
        ConfigManager.set("disabled-commands", new ArrayList<>(instance.disabledCommands));
        ConfigManager.save();
    }

    public static boolean isOverridden(String command) {
        return instance.overriddenCommands.containsKey(command.toLowerCase());
    }

    public static Component getOverrideMessage(String command) {
        return instance.overriddenCommands.get(command.toLowerCase());
    }

    public static void addOverridden(String command, String message) {
        instance.overriddenCommands.put(command.toLowerCase(), ChatUtils.colorize(message));
        persistOverridden();
    }

    public static void removeOverridden(String command) {
        instance.overriddenCommands.remove(command.toLowerCase());
        persistOverridden();
    }

    private static void persistOverridden() {
        Map<String, String> raw = new LinkedHashMap<>();
        instance.overriddenCommands.forEach((cmd, component) -> raw.put(cmd, MiniMessage.miniMessage().serialize(component)));
        ConfigManager.set("overridden-commands", raw);
        ConfigManager.save();
    }

    public static boolean isTabCompleteDisabled(String command) {
        return instance.disabledTabComplete.contains(command.toLowerCase());
    }

    public static Set<String> getDisabledCommands() { return Collections.unmodifiableSet(instance.disabledCommands); }
    public static Set<String> getDisabledTabComplete() { return Collections.unmodifiableSet(instance.disabledTabComplete); }
    public static Map<String, Component> getOverriddenCommands() { return Collections.unmodifiableMap(instance.overriddenCommands); }
}