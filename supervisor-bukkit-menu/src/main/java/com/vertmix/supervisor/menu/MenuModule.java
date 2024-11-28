package com.vertmix.supervisor.menu;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.menu.api.Menu;
import com.vertmix.supervisor.menu.api.PagedMenu;
import com.vertmix.supervisor.menu.api.PlayerMenu;
import com.vertmix.supervisor.menu.listener.InteractionModifierListener;
import com.vertmix.supervisor.menu.listener.MenuListener;
import com.vertmix.supervisor.menu.service.PagedMenuProxyHandler;
import com.vertmix.supervisor.menu.service.PlayerMenuProxyHandler;
import com.vertmix.supervisor.menu.service.StaticMenuProxyHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@ModuleInfo(name = "menu")
public class MenuModule implements Module<Plugin> {

    private File folder = null;

    @Override
    public void onEnable(CoreProvider<Plugin> provider) {


        Bukkit.getPluginManager().registerEvents(new MenuListener(), provider.getSource());
        Bukkit.getPluginManager().registerEvents(new InteractionModifierListener(), provider.getSource());

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

            Menu menu = new StaticMenuProxyHandler(clazz, file).getInstance();
            menu.setup();
            menu.init();


            return menu;
        });

        Services.register(PlayerMenu.class, clazz -> {
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

            PlayerMenu menu = new PlayerMenuProxyHandler(clazz, file).getInstance();
            menu.setup();
            menu.init();
            return menu;
        });

        Services.register(PagedMenu.class, clazz -> {
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

            PagedMenu menu = new PagedMenuProxyHandler(clazz, file).getInstance();
            menu.setup();
            menu.init();
            return menu;
        });
    }

    @Override
    public void onDisable() {

    }
}
