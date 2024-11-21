package com.vertmix.supervisor.core.bukkit.menu;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.menu.IMenu;
import org.bukkit.Sound;

@Component
@Navigation(path = "/menus/server-selector.yml")
public interface ServerSelectorIMenu extends IMenu {

    @Override
    default void setup() {

        modifiers().disableAllInteractions();

        System.out.println("is this being called?");
        schema().add("#########");
        options().put("title", "demo");
        options().put("click_sound", Sound.AMBIENT_CAVE.name());

        set('#', new Icon(), event -> {
            System.out.println("Clicked!");
        });


    }


}