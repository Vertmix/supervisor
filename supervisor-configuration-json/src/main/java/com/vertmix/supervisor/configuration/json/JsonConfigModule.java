package com.vertmix.supervisor.configuration.json;

import com.vertmix.supervisor.configuration.ConfigModule;
import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

@ModuleInfo(name = "configuration-json")
@ModuleDependency(dependencies = { "configuration" } )
public class JsonConfigModule implements Module<Object> {

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        Services.register(JsonConfigService.class, new JsonConfigService());
    }

    @Override
    public void onDisable() {

    }
}
