package com.vertmix.supervisor.menu.api;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.core.terminable.Terminal;
import com.vertmix.supervisor.menu.entity.InteractionModifier;
import com.vertmix.supervisor.menu.menu.MenuModifier;
import com.vertmix.supervisor.menu.menu.Schema;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collection;
import java.util.Map;

public interface IMenu extends Terminal {

    void render();

    void set(char c, Icon icon);

    void set(char c, Icon icon, GuiAction<InventoryClickEvent> action);

    void setSlotAction(char c, GuiAction<InventoryClickEvent> action);

    void setSlotAction(int slot, GuiAction<InventoryClickEvent> action);

    Schema schema();

    MenuModifier modifiers();

    Map<String, Object> options();

    void open(Player player);

    void init();

    default void setup() {
    }

    default void fallback(Player player) {

    }

    default void disableAllInteractions() {
        modifiers().disableAllInteractions();
    }

    default void disableInteraction(InteractionModifier modifier) {
        modifiers().add(modifier);
    }

    default void addOption(String key, Object o) {
        options().put(key, o);
    }

    default void schema(Collection<String> collection) {
        schema().add(collection);
    }

    default void schema(String... objects) {
        schema().add(objects);
    }

    void addAll(Collection<Icon> collection);

}
