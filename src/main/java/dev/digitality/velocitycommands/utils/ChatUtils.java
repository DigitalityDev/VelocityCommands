package dev.digitality.velocitycommands.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatUtils {
    public static Component colorize(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
