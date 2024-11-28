package com.vertmix.supervisor.repository.redis;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;

@ModuleInfo(name = "repository-redis")
@ModuleDependency(dependencies = { "adapter" })
public class RedisModule implements Module<Object> {

    @Override
    public void onEnable(CoreProvider<Object> provider) {

    }

    @Override
    public void onDisable() {

    }
}
