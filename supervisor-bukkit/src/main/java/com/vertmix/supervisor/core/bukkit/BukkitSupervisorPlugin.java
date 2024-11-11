package com.vertmix.supervisor.core.bukkit;

import com.vertmix.supervisor.loader.SupervisorLoader;
import org.bukkit.plugin.java.JavaPlugin;

import static com.vertmix.supervisor.core.bukkit.BukkitProvider.bukkit;

public class BukkitSupervisorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        SupervisorLoader.register(bukkit(this));
    }

    @Override
    public void onDisable() {

    }
}
