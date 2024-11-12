package com.vertmix.supervisor.configuration.yml;

import com.vertmix.supervisor.configuration.ConfigModule;
import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

@ModuleDependency(clazz = ConfigModule.class)
public class YmlConfigModule implements Module<Object> {

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        Services.register(YamlConfigService.class, new YamlConfigService());
    }

    @Override
    public void onDisable() {

    }
}
