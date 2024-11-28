package com.vertmix.supervisor.instance.repository;

import com.vertmix.supervisor.instance.api.Instance;
import com.vertmix.supervisor.instance.model.InstanceData;
import com.vertmix.supervisor.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public interface InstanceRepository extends Repository<InstanceData> {

    default List<Instance<?>> instances() {
        return new ArrayList<>();
    }
}
