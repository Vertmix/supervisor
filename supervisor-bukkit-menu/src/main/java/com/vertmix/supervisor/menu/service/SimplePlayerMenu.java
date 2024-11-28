package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.menu.PlayerMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class SimplePlayerMenu extends AbstractMenu<Player> implements PlayerMenu {

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
    public void render(Player player) {
        if (this.inventory == null || player == null || !player.isOnline()) {
            return;
        }

         schema.getCharacterMap().forEach((key, value) -> {
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
            }
        });
    }

    @Override
    public void open(Player player) {
        this.inventory = Bukkit.createInventory(player, 9, Component.text((String) options.getOrDefault("title", "Menu")));
        render(player);

        player.openInventory(this.inventory);
    }


    @Override
    public void close() throws IOException {

    }
}