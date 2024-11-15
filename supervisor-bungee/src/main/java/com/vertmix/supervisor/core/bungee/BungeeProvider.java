package com.vertmix.supervisor.core.bungee;

import com.vertmix.supervisor.core.CoreProvider;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeProvider extends CoreProvider<Plugin> {

    public BungeeProvider(Plugin source) {
        super(source.getDataFolder().toPath(), source);
    }

    public static CoreProvider<Plugin> bungee(Plugin plugin) {
        return new BungeeProvider(plugin);
    }

    @Override
    public void log(String str) {
        source.getLogger().info(str);
    }

    @Override
    public void error(String str) {
        source.getLogger().severe(str);
    }
}
