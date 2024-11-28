package com.vertmix.supervisor.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.reflection.ReflectionUtil;

import java.util.Collection;

import static com.vertmix.supervisor.loader.util.ComponentUtil.registerComponents;
import static com.vertmix.supervisor.loader.util.ModuleUtil.loadModules;

public class SupervisorLoader {

    public static void register(CoreProvider<?> provider, Object... objects) {
        String packageName = provider.getClass().getPackage().getName();
        ClassLoader classLoader = provider.getClass().getClassLoader();

        Collection<Class<?>> classes = ReflectionUtil.findClasses(packageName, classLoader);

        loadModules(classes, provider);

        for (Object object : objects)
            Services.register(object.getClass(), object);

        registerComponents(classes);
    }

    public static void disable() {
        Services.kill();
    }

}