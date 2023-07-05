package dev.digitality.velocitycommands.utils;

import dev.digitality.velocitycommands.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatUtils {
    public static Component colorize(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static Component getPrefix() {
        return colorize(ConfigManager.getConfig().getString("prefix"));
    }
}
