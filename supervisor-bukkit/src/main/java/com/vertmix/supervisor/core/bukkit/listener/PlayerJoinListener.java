//package com.vertmix.supervisor.core.bukkit.listener;
//
//import com.destroystokyo.paper.event.player.PlayerJumpEvent;
//import com.vertmix.supervisor.core.annotation.Component;
//import com.vertmix.supervisor.core.bukkit.menu.PlayerLevelMenu;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.plugin.Plugin;
//
//@Component
//public class PlayerJoinListener implements Listener {
//
//    private final PlayerLevelMenu playerLevelMenu;
//
//    public PlayerJoinListener(PlayerLevelMenu playerLevelMenu) {
//        this.playerLevelMenu = playerLevelMenu;
//    }
//
//    @EventHandler
//    public void onJump(PlayerJumpEvent event) {
//        final Player player = event.getPlayer();
//        System.out.println("listening?");
//
//        playerLevelMenu.open(player);
//    }
//}
