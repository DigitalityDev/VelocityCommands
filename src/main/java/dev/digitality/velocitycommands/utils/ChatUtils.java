package dev.digitality.velocitycommands.utils;

import dev.digitality.velocitycommands.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class ChatUtils {

    private static final String DEFAULT_PREFIX = "<gradient:#0BC1FB:#1AFDFD>VelocityCommands</gradient> <dark_gray>| ";

    private ChatUtils() {}

    public static Component colorize(String text) {
        if (text == null) return Component.empty();
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static Component prefixed(String msg) {
        return colorize(ConfigManager.getString("prefix", DEFAULT_PREFIX) + msg);
    }
}
