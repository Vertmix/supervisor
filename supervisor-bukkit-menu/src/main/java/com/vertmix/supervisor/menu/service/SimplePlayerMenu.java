package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

public class SimplePlayerMenu extends AbstractMenu implements IMenu {

    private Inventory inventory;

    public SimplePlayerMenu(File file) {
        super(file);
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
    }

    @Override
    public void render() {
        if (this.inventory == null) {
            return;
        }

        schema.getCharacterMap().forEach((key, value) -> {
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, icon.getItemstack());
            }
        });
    }

    @Override
    public void open(Player player) {
        inventory = Bukkit.createInventory(player, schema.size() * 9, options.getOrDefault("title", "Menu").toString());
        render();
        player.openInventory(inventory);
    }

    @Override
    public void close() throws IOException {

    }
}