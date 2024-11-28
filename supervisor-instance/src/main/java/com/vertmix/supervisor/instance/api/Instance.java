package com.vertmix.supervisor.instance.api;

import com.vertmix.supervisor.instance.model.InstanceData;

import java.util.List;

public interface Instance<T> {

    InstanceData getInstanceData();

    List<T> getPlayers();

    void teleport(T player, String instanceId, double x, double y, double z, float yaw, float pitch);

    void msg(T player, String text);
}
