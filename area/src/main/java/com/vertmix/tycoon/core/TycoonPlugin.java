package com.vertmix.tycoon.core;

import co.aikar.commands.PaperCommandManager;
import com.vertmix.supervisor.core.bukkit.BukkitModuleProvider;
import com.vertmix.supervisor.loader.SupervisorLoader;
import com.vertmix.supervisor.module.loader.ModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.vertmix.supervisor.core.bukkit.BukkitProvider.bukkit;

public class TycoonPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            SupervisorLoader.register(bukkit(this), new PaperCommandManager(this));
            ModuleLoader.register(new BukkitModuleProvider(getDataFolder().toPath(), this));
        }, 30L);

    }

    @Override
    public void onDisable() {

    }
}
