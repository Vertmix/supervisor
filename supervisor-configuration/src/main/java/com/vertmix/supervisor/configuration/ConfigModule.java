package com.vertmix.supervisor.configuration;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConfigModule implements Module<Object> {

    private static CoreProvider<Object> origin;

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        origin = provider;
        System.out.println("Loaded configuration module");

        Services.registerProcess(Configuration.class);
        Services.registerConsumer(configObject -> {
            if (configObject.getClass().isAnnotationPresent(Configuration.class)) {
                processConfigObject(configObject);
            }
        });
    }

    private void processConfigObject(Object configObject) {
        Class<?> clazz = configObject.getClass();
        Configuration configuration = clazz.getAnnotation(Configuration.class);

        File configDirectory = new File(origin.getPath().toFile(), configuration.path());
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
            throw new RuntimeException("Error instantiating configuration class: " + clazz.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No suitable constructor found for configuration class: " + clazz.getName(), e);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("Configuration module disabled.");
    }

    public static <T> T registerConfig(Class<T> clazz, ConfigService configService, String relativePath) {
        if (origin == null) {
            throw new IllegalStateException("CoreProvider is not initialized. Ensure ConfigModule is enabled before calling registerConfig.");
        }

        File configFile = new File(origin.getPath().toFile(), relativePath);
        File configDirectory = configFile.getParentFile();

        if (!configDirectory.exists() && !configDirectory.mkdirs()) {
            throw new IllegalStateException("Failed to create configuration directory at: " + configDirectory.getPath());
        }

        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(); // Use a generic constructor
            constructor.setAccessible(true);
            T configInstance = constructor.newInstance(); // Create a typed instance

            configService.register(clazz, configInstance, configFile); // Pass typed instance
            Services.register(clazz, configInstance);
            return configInstance;
            // Register with Services
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error instantiating configuration class: " + clazz.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No suitable constructor found for configuration class: " + clazz.getName(), e);
        }
    }

}
