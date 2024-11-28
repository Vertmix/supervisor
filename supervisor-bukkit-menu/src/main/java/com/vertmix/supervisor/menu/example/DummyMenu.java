package com.vertmix.supervisor.menu.example;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.entity.InteractionModifier;
import com.vertmix.supervisor.menu.menu.Menu;
import org.bukkit.Material;
import org.bukkit.Sound;

@Component
@Navigation(path = "/menus/dummy.yml")
public interface DummyMenu extends Menu {

    @Override
    default void setup() {
        schema("####@####");

        set('#', new Icon(Material.STONE), event -> {
            event.getWhoClicked().playSound((Sound) options().getOrDefault("sound", Sound.BLOCK_NOTE_BLOCK_BANJO));
        });
        set('@', new Icon(Material.APPLE), event -> {});

        disableAllInteractions();
        disableInteraction(InteractionModifier.PREVENT_ITEM_DROP);

        addOption("sound", Sound.BLOCK_NOTE_BLOCK_BANJO);
    }
}
