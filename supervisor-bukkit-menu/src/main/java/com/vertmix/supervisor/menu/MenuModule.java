package com.vertmix.supervisor.menu;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MenuModule implements Module<Plugin> {

    private File folder = null;

    @Override
    public void onEnable(CoreProvider<Plugin> provider) {
        folder = provider.getPath().toFile();
        Services.register(Menu.class, clazz -> {
            File file = folder;
            Navigation navigation = clazz.getAnnotation(Navigation.class);
            if (navigation != null) {
                file = new File(provider.getPath().toFile(), navigation.path());
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
                    throw new IllegalStateException("Failed to create parent directory at: " + parentDir.getPath());
                }

                try {
                    if (!file.exists() && !file.createNewFile()) {
                        throw new IllegalStateException("Failed to create configuration file at: " + file.getPath());
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("An error occurred while creating the file: " + file.getPath(), e);
                }
            }

            Menu menu = new MenuProxyHandler(clazz, file).getInstance();
            menu.setup();
            menu.init();
            return menu;
        });

        Bukkit.getPluginManager().registerEvents(new MenuListener(), provider.getSource());
    }

    @Override
    public void onDisable() {

    }
}
