package com.vertmix.supervisor.instance.bukkit.service;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.instance.bukkit.configuration.BukkitInstanceConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class SyncService implements Listener {

    private final BukkitInstanceConfig instanceConfig;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if ((boolean) instanceConfig.options.getOrDefault("inventory-sync", false)) {
            player.getInventory().clear();
            player.getInventory().setContents(null);
        }
        if ((boolean) instanceConfig.options.getOrDefault("health-sync", false)) {
            player.getInventory().setContents(null);
        }
        if ((boolean) instanceConfig.options.getOrDefault("level-sync", false)) {
            player.getInventory().setContents(null);
        }
        if ((boolean) instanceConfig.options.getOrDefault("exp-sync", false)) {
            player.getInventory().setContents(null);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        if ((boolean) instanceConfig.options.getOrDefault("inventory-sync", false)) {
            player.getInventory().setContents(null);
            completableFutures.add(CompletableFuture.supplyAsync(() -> {

                return null;
            }));

        }
        if ((boolean) instanceConfig.options.getOrDefault("health-sync", false)) {
            player.getInventory().setContents(null);
        }
        if ((boolean) instanceConfig.options.getOrDefault("level-sync", false)) {
            player.getInventory().setContents(null);
        }
        if ((boolean) instanceConfig.options.getOrDefault("exp-sync", false)) {
            player.getInventory().setContents(null);
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).complete(null);
    }
}
