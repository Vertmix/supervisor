package com.vertmix.supervisor.core.bukkit;

import com.vertmix.supervisor.core.module.ModuleProvider;
import org.bukkit.plugin.Plugin;

import java.nio.file.Path;

public class BukkitModuleProvider extends ModuleProvider<Plugin> {

    public BukkitModuleProvider(Path path, Plugin origin) {
        super(path, origin);
    }

    @Override
    public void log(String str) {
    }

    @Override
    public void error(String str) {

    }
}
