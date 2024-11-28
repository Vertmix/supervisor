package com.vertmix.supervisor.core.bukkit.menu;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.api.PagedMenu;
import com.vertmix.supervisor.menu.entity.InteractionModifier;
import org.bukkit.Material;

public interface PagesMenu extends PagedMenu {

    @Override
    default void setup() {

        schema(
                "#########",
                "<@@@@@@@>",
                "#########"
        );

        addOption("title", "example");

        set('#', new Icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE));



        disableAllInteractions();
        disableInteraction(InteractionModifier.PREVENT_ITEM_DROP);

    }
}
