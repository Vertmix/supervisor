package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import com.vertmix.supervisor.menu.util.Pager;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SimplePagedMenu<T> extends AbstractMenu<T> implements InventoryHolder, IMenu<T> {

    private static final char PAGED_KEY = '@';
    private static final char NEXT_KEY = '>';
    private static final char BACK_KEY = '<';

    protected Inventory inventory;
    private final Supplier<Collection<Icon>> supplier;
    private transient int page = 1;

    public SimplePagedMenu(File file, Supplier<Collection<Icon>> supplier) {
        super(file);
        this.supplier = supplier;
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
        render(null);

        this.charActions.put(NEXT_KEY, event -> {
            page++;
            render(null);
        });

        this.charActions.put(BACK_KEY, event -> {
            page--;
            render(null);
        });
    }
    @Override
    public void render(Object o) {
        Set<Integer> slots = schema.getCharacterMap().get(PAGED_KEY);
        Pager<Icon> pager = new Pager<>(supplier.get().stream().toList(), slots.size());
        List<Icon> icons = pager.getPage(page);
        // icons.get(x)
        slots.forEach(x -> inventory.setItem(x, new ItemStack(Material.DRAGON_EGG)));

        schema.getCharacterMap().forEach((key, value) -> {
            if (key == PAGED_KEY) return;
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
            }
        });

    }

    @Override
    public void open(Player player) {
        inventory = Bukkit.createInventory(this, 9, Component.text((String) options.getOrDefault("title", "Menu")));
        render(null);
        player.openInventory(this.inventory);
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
