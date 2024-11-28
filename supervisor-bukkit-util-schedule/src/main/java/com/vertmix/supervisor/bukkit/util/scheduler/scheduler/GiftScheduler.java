package com.vertmix.supervisor.bukkit.util.scheduler.scheduler;

import com.vertmix.supervisor.bukkit.util.scheduler.AbstractScheduler;
import com.vertmix.supervisor.bukkit.util.scheduler.HourMinute;
import com.vertmix.supervisor.core.annotation.Component;
import org.bukkit.plugin.Plugin;

import java.util.List;

@Component
public class GiftScheduler extends AbstractScheduler  {

    public GiftScheduler(Plugin plugin) {
        super(plugin, List.of(HourMinute.now()));
    }

    @Override
    public void run() {
        
    }
}
