package com.vertmix.supervisor.configuration;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConfigModule implements Module<Object> {

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        System.out.println("Loaded configuration module");
        Services.registerProcess(Configuration.class);
        Services.registerConsumer(o -> {
            if (o.getClass().isAnnotationPresent(Configuration.class)) {
                Class<?> clazz = o.getClass();
                Configuration configuration = clazz.getAnnotation(Configuration.class);
                File configDirectory = new File(provider.getPath().toFile(), configuration.path());

                if (!configDirectory.exists() && !configDirectory.mkdirs()) {
                    throw new IllegalStateException("Failed to create configuration directory at: " + configDirectory.getPath());
                }

                File configFile = new File(configDirectory, configuration.fileName());
                ConfigService configService = Services.loadIfPresent(configuration.service());

                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object configInstance = constructor.newInstance();

                    configService.register((Class<Object>) clazz, configInstance, configFile);
                    Services.register(clazz, configInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
