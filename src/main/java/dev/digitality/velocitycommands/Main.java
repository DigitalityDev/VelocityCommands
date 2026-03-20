package dev.digitality.velocitycommands;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.digitality.velocitycommands.commands.BlockCommands;
import dev.digitality.velocitycommands.listeners.CommandListener;
import dev.digitality.velocitycommands.managers.CommandRegistry;
import dev.digitality.velocitycommands.managers.ConfigManager;
import lombok.Getter;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "velocitycommands",
        name = "VelocityCommands",
        version = "2.0.0",
        description = "Block, disable and override commands on your Velocity proxy.",
        authors = {"Digitality"}
)

public final class Main {
    @Getter
    private final ProxyServer server;
    @Getter
    private final Logger logger;
    @Getter
    private final Path dataDirectory;


    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent e) {
        new ConfigManager(dataDirectory, logger);
        new CommandRegistry();

        ConfigManager.load();
        CommandRegistry.reload();

        server.getEventManager().register(this, new CommandListener());

        server.getCommandManager().register(
                server.getCommandManager().metaBuilder("vcmds")
                        .aliases("velocitycommands", "vcmd", "velocitycmds", "vscmd")
                        .build(),
                new BlockCommands()
        );
        logger.info("VelocityCommands has been enabled!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
        logger.info("VelocityCommands disabled.");
    }
}