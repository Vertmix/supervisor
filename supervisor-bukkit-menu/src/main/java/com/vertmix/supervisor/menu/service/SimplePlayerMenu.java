package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
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
    public void close() throws IOException {

    }
}