package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import com.vertmix.supervisor.menu.api.PagedMenu;
import com.vertmix.supervisor.menu.util.Pager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimplePagedMenu extends AbstractMenu implements InventoryHolder, IMenu {
    protected Inventory inventory;
    protected Pager pager;
    private int page = 1;

    public SimplePagedMenu(File file) {
        super(file);
    }

    @Override
    public void init() {
        loadFile();
        schema().build();

        set('>', new Icon(Material.ARROW, 1), event -> {
            page++;
            render();
        });

        set('<', new Icon(Material.BARRIER, 1), event -> {
            page--;
            render();
        });

        inventory = Bukkit.createInventory(this, schema.size() * 9, "Example");
        render();
    }

    @Override
    public void render() {
        List<Integer> indexes = schema.getCharacterMap().get('@');
        this.pager = new Pager(icons, indexes.size());
        List<Icon> icons = pager.getPage(page);

        int i = 0;
        for (int x : indexes) {
            inventory.setItem(x, icons.get(i).getItemstack());
            i++;
        }


        schema.getCharacterMap().forEach((key, value) -> {
            if (key == '@') return;
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, icon.getItemstack());
            }
        });

    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            throw new NullPointerException("Could not create menu");
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
