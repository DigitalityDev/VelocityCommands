package dev.digitality.velocitycommands;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.digitality.velocitycommands.commands.ReloadCommand;
import dev.digitality.velocitycommands.managers.CommandOverrideManager;
import dev.digitality.velocitycommands.managers.ConfigManager;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id="velocitycommands", name="VelocityCommands", version="1.0.0",
        description="A plugin that allows you to run commands from the console.", authors={"Digitality"})
@Getter
public final class DigitalMain {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Getter
    private static DigitalMain instance;

    @Inject
    public DigitalMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        instance = this;

        logger.info("Plugin was successfully loaded!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register("reload-cmds", new ReloadCommand());

        ConfigManager.reloadConfigs();
        getServer().getEventManager().register(DigitalMain.getInstance(), new CommandOverrideManager());
    }
}