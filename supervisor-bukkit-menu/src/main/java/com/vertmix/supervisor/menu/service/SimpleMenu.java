package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class SimpleMenu extends AbstractMenu implements InventoryHolder, IMenu {

    protected Inventory inventory;

    public SimpleMenu(File file) {
        super(file);
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
        render();
    }

    @Override
    public void render() {
        inventory = Bukkit.createInventory(this, 9, Component.text((String) options.getOrDefault("title", "Menu")));

        schema.getCharacterMap().forEach((key, value) -> {
            System.out.println(key + " RENDERING");
            Icon icon = items.get(key);

            for (Integer slot : value) {
                System.out.println("set slot ?" + icon.material);
                inventory.setItem(slot, icon.getItemstack());
            }
        });

    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            throw new NullPointerException("Could not create menu");
        render();
        return this.inventory;
    }

    @Override
    public void open(Player player) {
        player.openInventory(getInventory());
    }

    @Override
    public void close() throws IOException {

    }
}
