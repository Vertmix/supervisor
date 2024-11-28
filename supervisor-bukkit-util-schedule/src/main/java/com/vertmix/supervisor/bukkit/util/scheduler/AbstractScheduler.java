package com.vertmix.supervisor.bukkit.util.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.ZoneId;
import java.util.Collection;

public abstract class AbstractScheduler implements Scheduler {

    protected final Schedule schedule;

    public AbstractScheduler(Plugin plugin, Collection<HourMinute> hourMinutes) {
        this(plugin, ZoneId.systemDefault(), hourMinutes);
    }

    public AbstractScheduler(Plugin plugin, ZoneId zoneId, Collection<HourMinute> hourMinutes) {
        this.schedule = new Schedule(zoneId, hourMinutes);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (schedule.isNextEventTime()) {
                schedule.update();
                run();
            }
        }, 10L, 10L);
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
