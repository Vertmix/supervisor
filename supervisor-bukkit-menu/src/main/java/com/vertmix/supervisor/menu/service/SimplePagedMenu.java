package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import com.vertmix.supervisor.menu.util.Pager;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Supplier;

public class SimplePagedMenu extends AbstractMenu implements InventoryHolder, IMenu {

    private final Supplier<Collection<Icon>> supplier;
    protected Inventory inventory;

    protected Pager pager;

    public SimplePagedMenu(File file, Supplier<Collection<Icon>> supplier) {
        super(file);
        this.supplier = supplier;
        this.pager = new Pager(this, supplier.get().stream().toList());
    }

    @Override
    public void init() {
        loadFile();
        schema().build();

        set(pager.getNEXT_KEY(), new Icon(Material.ARROW, 1), event -> pager.next());

        set(pager.getBACK_KEY(), new Icon(Material.ARROW, 1), event -> pager.previous());

        render();
    }

    @Override
    public void render() {

        schema.getCharacterMap().forEach((key, value) -> {
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
    public void close() throws IOException {

    }
}
