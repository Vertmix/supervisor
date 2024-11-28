package com.vertmix.supervisor.core.bukkit.menu;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.entity.InteractionModifier;
import com.vertmix.supervisor.menu.menu.PlayerMenu;

public interface PlayerLevelMenu extends PlayerMenu {

    @Override
    default void setup() {

        schema(
                "#########",
                "#########",
                "#########"
        );

        addOption("title", "example");

        set('#', new Icon(), event -> {
            System.out.println("Clicked!");
        });

        disableAllInteractions();
        disableInteraction(InteractionModifier.PREVENT_ITEM_DROP);
    }
}
