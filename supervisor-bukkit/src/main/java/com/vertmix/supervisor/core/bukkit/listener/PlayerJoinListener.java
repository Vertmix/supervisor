package com.vertmix.supervisor.core.bukkit.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.bukkit.menu.ServerSelectorIMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Component
public class PlayerJoinListener implements Listener {

    private final ServerSelectorIMenu serverSelectorMenu;

    public PlayerJoinListener(ServerSelectorIMenu serverSelectorMenu) {
        this.serverSelectorMenu = serverSelectorMenu;
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        final Player player = event.getPlayer();
        System.out.println("listening?");
        serverSelectorMenu.open(player);
    }
}
