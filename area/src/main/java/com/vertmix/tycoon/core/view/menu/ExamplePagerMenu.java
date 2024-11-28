package com.vertmix.tycoon.core.view.menu;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.PagedMenu;
import org.bukkit.Material;

import java.util.List;

@Component
@Navigation(path = "/menu/pager.yml")
public interface ExamplePagerMenu extends PagedMenu {

    @Override
    default void setup() {
        addAll(List.of(new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE), new Icon(Material.STONE) ,new Icon(Material.STONE)));

        schema("#@@@@@@@@>");
        setSlotAction('@', event -> {
            event.getWhoClicked().sendMessage("hey");
        });
        set('#', new Icon(Material.BLACK_BED));
        disableAllInteractions();
    }
}
