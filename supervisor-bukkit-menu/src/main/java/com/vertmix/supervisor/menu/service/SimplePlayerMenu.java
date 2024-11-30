package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.core.bukkit.item.Text;
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
        System.out.println("before Schema: " + schema.size());

        loadFile();
        schema().build();

        System.out.println("after Schema: " + schema.size());

        this.inventory = Bukkit.createInventory(null, Math.max(1, schema.size()) * 9, Text.translate(options.getOrDefault("title", "Menu").toString()));
    }

    @Override
    public void render() {
        if (this.inventory == null) {
            System.out.println("null inventory");
            return;
        }

        System.out.println("rendering inventory");

        schema.getCharacterMap().forEach((key, value) -> {
            Icon icon = items.get(key);

            System.out.println(key + " " + icon.material);

            for (Integer slot : value) {
                inventory.setItem(slot, icon.getItemstack());
            }
        });
    }

    @Override
    public void open(Player player) {
        render();
        player.openInventory(inventory);
    }

    @Override
    public void close() throws IOException {

    }
}