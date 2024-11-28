package com.vertmix.supervisor.instance.bukkit;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.instance.api.Instance;
import com.vertmix.supervisor.instance.model.InstanceData;
import com.vertmix.supervisor.instance.bukkit.configuration.BukkitInstanceConfig;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class BukkitInstance implements Instance<Player> {

    private final BukkitInstanceConfig bukkitInstanceConfig;


    @Override
    public InstanceData getInstanceData() {
        return new InstanceData(bukkitInstanceConfig.host, bukkitInstanceConfig.id, bukkitInstanceConfig.port, bukkitInstanceConfig.options);
    }

    @Override
    public List<Player> getPlayers() {
        return new LinkedList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public void teleport(Player player, String instanceId, double x, double y, double z, float yaw, float pitch) {

    }

    @Override
    public void msg(Player player, String text) {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        InstanceData instanceData;
    }
}
