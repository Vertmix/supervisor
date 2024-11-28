package com.vertmix.tycoon.core.view.menu;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Component
@Navigation(path = "/menus/example.yml")
public interface ExampleMenu extends Menu {

    @Override
    default void setup() {
        schema("####@####");
        set('#', new Icon(Material.STONE), event -> event.getWhoClicked().sendMessage("Dummy"));
        set('@', new Icon(Material.APPLE), event -> event.getWhoClicked().sendMessage("Really"));
        disableAllInteractions();
    }

    @Override
    default void fallback(Player player) {

    }
}
