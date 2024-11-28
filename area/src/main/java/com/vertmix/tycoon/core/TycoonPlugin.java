package com.vertmix.tycoon.core;

import co.aikar.commands.PaperCommandManager;
import com.vertmix.supervisor.loader.SupervisorLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.vertmix.supervisor.core.bukkit.BukkitProvider.bukkit;

public class TycoonPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            //SupervisorLoader.register(bukkit(this), new PaperCommandManager(this));
        }, 30L);

    }

    @Override
    public void onDisable() {

    }
}
