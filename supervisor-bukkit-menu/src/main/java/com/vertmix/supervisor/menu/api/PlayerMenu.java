package com.vertmix.supervisor.menu.api;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Function;

public interface PlayerMenu extends IMenu {

    void render(Inventory inventory);

    void set(Inventory inventory, char c, Icon icon);

    void set(Inventory inventory, char c, Icon icon, GuiAction<InventoryClickEvent> action);

    void setSlotAction(Inventory inventory, char c, GuiAction<InventoryClickEvent> action);

    void setSlotAction(Inventory inventory, int slot, GuiAction<InventoryClickEvent> action);

    void render(Player player);

    void set(Player player, char c, Icon icon);

    void set(Player player, char c, Icon icon, GuiAction<InventoryClickEvent> action);

    void setSlotAction(Player player, char c, GuiAction<InventoryClickEvent> action);

    void setSlotAction(Player player, int slot, GuiAction<InventoryClickEvent> action);

    void replace(String key, Function<Player, String> function);
}
