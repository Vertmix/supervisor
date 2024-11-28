package com.vertmix.supervisor.loader.util;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;

import java.util.*;

public class ModuleUtil {

    public static Set<Module> loadModules(Collection<Class<?>> classes, CoreProvider<?> provider) {
        Map<String, Module> modulesMap = new HashMap<>();
        Map<String, List<String>> moduleDependencies = new HashMap<>();
        Set<Module> enabledModules = new LinkedHashSet<>();

        try {
            // Initialize modules and dependencies
            for (Class<?> clazz : classes) {
                if (clazz.isInterface() || clazz.getInterfaces().length == 0 || clazz.getInterfaces()[0] != Module.class) {
                    continue;
                }

                Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                ModuleInfo moduleInfo = clazz.getAnnotation(ModuleInfo.class);
                ModuleDependency moduleDependency = clazz.getAnnotation(ModuleDependency.class);

                if (moduleInfo != null) {
                    modulesMap.put(moduleInfo.name(), module);
                    if (moduleDependency != null) {
                        moduleDependencies.put(moduleInfo.name(), Arrays.asList(moduleDependency.dependencies()));
                    } else {
                        moduleDependencies.put(moduleInfo.name(), Collections.emptyList());
                    }
                }
            }

            // Enable modules in dependency order
            Set<String> visited = new HashSet<>();
            for (String moduleName : modulesMap.keySet()) {
                enableModuleWithDependencies(moduleName, modulesMap, moduleDependencies, enabledModules, visited, provider);
            }

        } catch (Exception e) {
            System.err.println("Failed to initialize modules: " + e.getMessage());
            e.printStackTrace();
        }

        return enabledModules;
    }

    private static void enableModuleWithDependencies(String moduleName, Map<String, Module> modulesMap,
                                                     Map<String, List<String>> moduleDependencies, Set<Module> enabledModules,
                                                     Set<String> visited, CoreProvider<?> provider) throws Exception {
        if (visited.contains(moduleName)) {
            throw new IllegalStateException("Circular dependency detected involving module: " + moduleName);
        }

        if (modulesMap.containsKey(moduleName) && enabledModules.stream().noneMatch(m -> m.getClass().getAnnotation(ModuleInfo.class).name().equals(moduleName))) {
            visited.add(moduleName);
            List<String> dependencies = moduleDependencies.get(moduleName);
            if (dependencies != null) {
                for (String dependency : dependencies) {
                    enableModuleWithDependencies(dependency, modulesMap, moduleDependencies, enabledModules, visited, provider);
                }
            }
            visited.remove(moduleName);

            Module module = modulesMap.get(moduleName);
            if (module != null) {
                module.onEnable(provider);
                enabledModules.add(module);
                System.out.println("Enabled module: " + moduleName);
            }
        }
    }
}