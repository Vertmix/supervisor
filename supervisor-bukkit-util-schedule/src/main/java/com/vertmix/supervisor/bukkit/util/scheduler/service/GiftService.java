package com.vertmix.supervisor.bukkit.util.scheduler.service;

import com.vertmix.supervisor.bukkit.util.scheduler.scheduler.GiftScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GiftService {

    private final GiftScheduler giftScheduler;

    public GiftService(Plugin plugin, GiftScheduler giftScheduler) {
        this.giftScheduler = giftScheduler;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (giftScheduler.getSchedule().getSecondsUntilNext() % 10 == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage("Example");
                }
            }

        }, 20L, 20L);
    }


}
