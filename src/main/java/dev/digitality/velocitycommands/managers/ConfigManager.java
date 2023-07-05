package dev.digitality.velocitycommands.managers;

import com.mojang.brigadier.Command;
import com.velocitypowered.api.event.Subscribe;
import dev.digitality.velocitycommands.DigitalMain;
import dev.digitality.velocitycommands.utils.ChatUtils;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {
    @Getter
    private static Configuration config;

    public static void reloadConfigs() {
        CommandOverrideManager.getDisabledCommands().clear();
        CommandOverrideManager.getOverridenCommands().clear();

        config = loadConfig("commands.yml");

        ConfigManager.getConfig().getSection("overriden-commands").getKeys().forEach(key -> CommandOverrideManager.getOverridenCommands().put(key.toLowerCase(), ChatUtils.colorize(ConfigManager.getConfig().getString("overriden-commands." + key))));
        CommandOverrideManager.getDisabledCommands().addAll(ConfigManager.getConfig().getStringList("disabled-commands").stream().map(String::toLowerCase).toList());
    }

    @Subscribe
    public static Configuration loadConfig(String fileName) {
        if (!DigitalMain.getInstance().getDataDirectory().toFile().exists()) {
            DigitalMain.getInstance().getDataDirectory().toFile().mkdirs();
        }

        File file = new File(DigitalMain.getInstance().getDataDirectory().toFile(), fileName);
        // Create default config
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            try (InputStream in = ConfigManager.class.getResourceAsStream(fileName)) {
                if (in != null)
                    Files.copy(in, file.toPath());
                else
                    Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(DigitalMain.getInstance().getDataDirectory().toFile(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

