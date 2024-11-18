package com.vertmix.supervisor.menu;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.core.terminable.Terminal;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Map;

public interface Menu extends Terminal {

    void render();
    default void setup() {}
    default void callback() {}
    void set(char c, Icon icon, GuiAction<InventoryClickEvent> action);
    List<String> schema();
    Map<String, Object> options();
    void open(Player player);

    void init();
}
