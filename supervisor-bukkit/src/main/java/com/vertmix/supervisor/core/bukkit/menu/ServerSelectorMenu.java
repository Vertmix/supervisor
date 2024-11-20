package com.vertmix.supervisor.core.bukkit.menu;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.menu.Menu;
import org.bukkit.Sound;

import java.util.List;

@Component
@Navigation(path = "/menus/server-selector.yml")
public interface ServerSelectorMenu extends Menu {

    @Override
    default void setup() {

        System.out.println("is this being called?");
        schema().add(List.of("#########"));
        options().put("title", "demo");
        options().put("click_sound", Sound.AMBIENT_CAVE.name());

        set('#', new Icon(), event -> {
            System.out.println("Clicked!");
        });


    }

    @Override
    default void render() {

    }


}
