package com.vertmix.supervisor.loader.util;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.api.ClassProcessor;
import com.vertmix.supervisor.core.module.Module;

import java.util.*;

public class ModuleUtil {

    public static Set<Module> loadModules(Collection<Class<?>> classes, CoreProvider<?> provider) {
        System.out.println("Starting module loading process...");
        System.out.println("Classes to process: " + classes);

        Map<String, Module> modulesMap = new HashMap<>();
        Map<String, List<String>> moduleDependencies = new HashMap<>();
        Set<Module> enabledModules = new LinkedHashSet<>();

        try {
            // Initialize modules and dependencies
            for (Class<?> clazz : classes) {
                System.out.println("Processing class: " + clazz.getSimpleName());

                // Skip non-Module classes or interfaces
                if (clazz.isInterface() || clazz.getInterfaces().length == 0 || clazz.getInterfaces()[0] != Module.class) {
                    System.out.println("Skipping class (not a Module): " + clazz.getSimpleName());
                    continue;
                }

                // Attempt to instantiate the module
                try {
                    Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                    ModuleInfo moduleInfo = clazz.getAnnotation(ModuleInfo.class);
                    ModuleDependency moduleDependency = clazz.getAnnotation(ModuleDependency.class);

                    if (moduleInfo != null) {
                        System.out.println("Found module: " + moduleInfo.name());
                        modulesMap.put(moduleInfo.name(), module);

                        if (moduleDependency != null) {
                            System.out.println("Module " + moduleInfo.name() + " depends on: " + Arrays.toString(moduleDependency.dependencies()));
                            moduleDependencies.put(moduleInfo.name(), Arrays.asList(moduleDependency.dependencies()));
                        } else {
                            System.out.println("Module " + moduleInfo.name() + " has no dependencies.");
                            moduleDependencies.put(moduleInfo.name(), Collections.emptyList());
                        }
                    } else {
                        System.out.println("Class " + clazz.getSimpleName() + " is missing @ModuleInfo.");
                    }
                } catch (NoSuchMethodException e) {
                    System.err.println("Class " + clazz.getSimpleName() + " does not have a no-argument constructor.");
                } catch (Exception e) {
                    System.err.println("Failed to instantiate module: " + clazz.getSimpleName());
                    e.printStackTrace();
                }
            }

            // Enable modules in dependency order
            Set<String> visited = new HashSet<>();
            for (String moduleName : modulesMap.keySet()) {
                System.out.println("Enabling module and dependencies: " + moduleName);
                enableModuleWithDependencies(moduleName, modulesMap, moduleDependencies, enabledModules, visited, provider, classes);
            }

        } catch (Exception e) {
            System.err.println("Failed to initialize modules: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Finished module loading process.");
        return enabledModules;
    }

    private static void enableModuleWithDependencies(String moduleName, Map<String, Module> modulesMap,
                                                     Map<String, List<String>> moduleDependencies, Set<Module> enabledModules,
                                                     Set<String> visited, CoreProvider<?> provider, Collection<Class<?>> classes) throws Exception {
        if (visited.contains(moduleName)) {
            System.err.println("Circular dependency detected involving: " + moduleName);
            throw new IllegalStateException("Circular dependency detected involving module: " + moduleName);
        }

        // Check if module is already enabled
        if (modulesMap.containsKey(moduleName) && enabledModules.stream().noneMatch(m -> m.getClass().getAnnotation(ModuleInfo.class).name().equals(moduleName))) {
            System.out.println("Attempting to enable module: " + moduleName);

            // Mark as visited
            visited.add(moduleName);

            // Resolve dependencies
            List<String> dependencies = moduleDependencies.get(moduleName);
            if (dependencies != null) {
                for (String dependency : dependencies) {
                    System.out.println("Module " + moduleName + " depends on: " + dependency);
                    enableModuleWithDependencies(dependency, modulesMap, moduleDependencies, enabledModules, visited, provider, classes);
                }
            }
            visited.remove(moduleName);

            // Enable the module
            Module module = modulesMap.get(moduleName);
            if (module != null) {
                System.out.println("Enabling module: " + moduleName);
                module.onEnable(provider);

                // Process classes with ClassProcessor
                if (module instanceof ClassProcessor processor) {
                    for (Class<?> clazz : classes) {
                        System.out.println("Processing class: " + clazz.getSimpleName() + " with module: " + moduleName);
                        processor.process(clazz);
                    }
                }

                enabledModules.add(module);
                System.out.println("Successfully enabled module: " + moduleName);
            }
        }
    }
}
