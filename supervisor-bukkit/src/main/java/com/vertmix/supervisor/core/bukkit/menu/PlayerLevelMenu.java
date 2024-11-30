package com.vertmix.supervisor.core.bukkit.menu;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.PlayerMenu;
import com.vertmix.supervisor.menu.entity.InteractionModifier;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Component
@Navigation(path = "/menus/playerMenu.yml")
public interface PlayerLevelMenu extends PlayerMenu {

    @Override
    default void setup() {

        schema(
                "#########",
                "#########",
                "#########"
        );

        addOption("title", "example");

        set('#', new Icon(Material.GOLD_BLOCK), event -> {
            System.out.println("Clicked!");
        });

        disableAllInteractions();
        disableInteraction(InteractionModifier.PREVENT_ITEM_DROP);

        replace("{player}", Player::getName);
    }
}
