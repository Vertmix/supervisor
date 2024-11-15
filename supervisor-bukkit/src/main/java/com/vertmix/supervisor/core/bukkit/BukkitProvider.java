package com.vertmix.supervisor.core.bukkit;

import com.vertmix.supervisor.core.CoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;

public class BukkitProvider extends CoreProvider<Plugin> {

    public BukkitProvider(Plugin source) {
        super(source.getDataFolder().toPath(), source);

        if (!Files.exists(source.getDataFolder().toPath())) {
            try {
                Files.createDirectories(source.getDataFolder().toPath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create plugin data folder: " + source.getDataFolder().toPath(), e);
            }
        }
    }

    public static CoreProvider<Plugin> bukkit(Plugin plugin) {
        return new BukkitProvider(plugin);
    }

    @Override
    public void log(String str) {
        Bukkit.getLogger().info(str);
    }

    @Override
    public void error(String str) {
        Bukkit.getLogger().severe(str);
    }
}
