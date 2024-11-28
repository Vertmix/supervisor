package com.vertmix.supervisor.mongo;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;
@ModuleInfo(name = "repository-json")
@ModuleDependency(dependencies = { "adapter", "repository-redis" })
public class MongoRedisCacheModule implements Module<Object> {


    @Override
    public void onEnable(CoreProvider<Object> provider) {

    }

    @Override
    public void onDisable() {

    }
}
