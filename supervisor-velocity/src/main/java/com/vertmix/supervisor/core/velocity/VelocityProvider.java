package com.vertmix.supervisor.core.velocity;

import com.vertmix.supervisor.core.CoreProvider;
import com.velocitypowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.logging.Logger;

public class VelocityProvider extends CoreProvider<PluginContainer> {

    private final Logger logger;

    public VelocityProvider(Path dataFolder, PluginContainer source, Logger logger) {
        super(dataFolder, source);
        this.logger = logger;
    }

    public static CoreProvider<PluginContainer> velocity(PluginContainer plugin, Path dataFolder, Logger logger) {
        return new VelocityProvider(dataFolder, plugin, logger);
    }

    @Override
    public void log(String str) {
        this.logger.info(str);
    }

    @Override
    public void error(String str) {
        this.logger.severe(str);
    }
}