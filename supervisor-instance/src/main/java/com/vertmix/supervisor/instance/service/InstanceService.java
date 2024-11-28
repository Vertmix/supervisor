package com.vertmix.supervisor.instance.service;

import com.vertmix.supervisor.instance.api.Balancer;
import com.vertmix.supervisor.instance.api.Instance;
import com.vertmix.supervisor.instance.repository.InstanceRepository;
import com.vertmix.supervisor.instance.api.strategy.DefaultBalancer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InstanceService<T> {

    private static final Balancer<Instance<?>> INSTANCE_BALANCER = new DefaultBalancer<>(x -> x.getPlayers().size());

    private final InstanceRepository instanceRepository;


    public Instance<?> getAvailableInstance() {
        return INSTANCE_BALANCER.balance(instanceRepository.instances());
    }
}
