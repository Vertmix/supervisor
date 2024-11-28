package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.IMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    public SimplePagedMenu(File file, Supplier<Collection<Icon>> supplier) {
        super(file);
        this.supplier = supplier;
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
        render();

//        this.charActions.put(nextKey, event -> {
//            page++;
//            render(null);
//        });
//
//        this.charActions.put(backKey, event -> {
//            page--;
//            render(null);
//        });
    }

    @Override
    public void render() {

        //todo
        //        Set<Integer> slots = schema.getCharacterMap().get(pagedKey);
//
//        Pager pager = new Pager(this, supplier.get().stream().toList(), slots.size());
//
//        List<Icon> icons = pager.getPage(page);
//        // icons.get(x)
//        slots.forEach(x -> inventory.setItem(x, new ItemStack(Material.DRAGON_EGG)));
//
//        schema.getCharacterMap().forEach((key, value) -> {
//            if (key == pagedKey) return;
//            Icon icon = items.get(key);
//
//            for (Integer slot : value) {
//                inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
//            }
//        });

    }

    @Override
    public void open(Player player) {
        inventory = Bukkit.createInventory(this, 9, Component.text((String) options.getOrDefault("title", "Menu")));
        render();
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
