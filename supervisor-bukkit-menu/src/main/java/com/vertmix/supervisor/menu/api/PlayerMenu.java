package com.vertmix.supervisor.menu.api;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface PlayerMenu extends IMenu {

    void replace(String key, Function<Player, String> function);
}
