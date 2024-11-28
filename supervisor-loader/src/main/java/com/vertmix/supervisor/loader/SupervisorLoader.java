package com.vertmix.supervisor.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.reflection.ReflectionUtil;

import java.util.Collection;
import java.util.Set;

import static com.vertmix.supervisor.loader.util.ComponentUtil.registerComponents;
import static com.vertmix.supervisor.loader.util.ModuleUtil.loadModules;

public class SupervisorLoader {

    public static void register(CoreProvider<?> provider, Object... objects) {
        String packageName = provider.getClazz().getPackage().getName();
        ClassLoader classLoader = provider.getClass().getClassLoader();

        Collection<Class<?>> classes = ReflectionUtil.findClasses(packageName, classLoader);

        Set<Module> modules = loadModules(classes, provider);
        if (modules.isEmpty()) {
            provider.log("No modules found");
        } else {
            modules.forEach(module -> {
                ModuleInfo moduleInfo = module.getClass().getAnnotation(ModuleInfo.class);
                provider.log("[Module] [" + moduleInfo == null ? "Unknown" : moduleInfo.name() + " ] has been successfully registered");
            });
            provider.log("Loaded " + modules.size() + "x modules");

        }


        for (Object object : objects)
            Services.register(object.getClass(), object);

        registerComponents(classes);
    }

    public static void disable() {
        Services.kill();
    }

}