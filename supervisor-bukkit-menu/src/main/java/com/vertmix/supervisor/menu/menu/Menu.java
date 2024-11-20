package com.vertmix.supervisor.menu.menu;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.core.terminable.Terminal;
import com.vertmix.supervisor.menu.item.GuiAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Map;

public interface Menu extends Terminal {

    void render();
    default void setup() {}
    default void callback() {}
    void set(char c, Icon icon, GuiAction<InventoryClickEvent> action);

    Schema schema();

    Map<String, Object> options();
    void open(Player player);

    void init();
}
