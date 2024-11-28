package com.vertmix.supervisor.module.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ModuleLoader {

    private static final Map<String, Module> loadedModules = new LinkedHashMap<>();
    private static final Map<String, List<String>> moduleDependencies = new HashMap<>();
    private static final Set<String> enabledModules = new HashSet<>();

    public static void register(CoreProvider<?> provider) {
        Path modulesPath = provider.getPath().resolve("modules");

        try {
            // Create the modules folder if it does not exist
            if (Files.notExists(modulesPath)) {
                Files.createDirectories(modulesPath);
            }

            // Clean up the directory: remove any files that are not .jar but keep folders
            try (Stream<Path> files = Files.list(modulesPath)) {
                files.filter(Files::isRegularFile)
                        .filter(file -> !file.toString().endsWith(".jar"))
                        .forEach(ModuleLoader::deleteFile);
            }

            // Load all .jar files and extract module dependencies
            try (Stream<Path> jarFiles = Files.list(modulesPath)) {
                List<Path> jarPaths = jarFiles.filter(file -> file.toString().endsWith(".jar")).toList();
                for (Path jarPath : jarPaths) {
                    try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();

                            if (name.endsWith(".class")) {
                                String className = name.replace('/', '.').replace(".class", "");
                                try {
                                    Class<?> clazz = Class.forName(className, true, ModuleLoader.class.getClassLoader());

                                    if (clazz.isAnnotationPresent(ModuleInfo.class) && Module.class.isAssignableFrom(clazz)) {
                                        Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                                        ModuleInfo moduleInfo = clazz.getAnnotation(ModuleInfo.class);
                                        ModuleDependency moduleDependency = clazz.getAnnotation(ModuleDependency.class);

                                        if (moduleDependency != null) {
                                            moduleDependencies.put(moduleInfo.name(), Arrays.asList(moduleDependency.dependencies()));
                                        } else {
                                            moduleDependencies.put(moduleInfo.name(), Collections.emptyList());
                                        }

                                        loadedModules.put(moduleInfo.name(), module);
                                    }
                                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                                    System.err.println("Failed to load class: " + className);
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to read jar file: " + jarPath);
                        e.printStackTrace();
                    }
                }
            }

            // Enable modules in dependency order
            for (String moduleName : loadedModules.keySet()) {
                enableModule(provider, moduleName, modulesPath);
            }

            System.out.println("Successfully loaded and enabled " + enabledModules.size() + " modules.");
        } catch (IOException e) {
            System.err.println("Failed to set up module loader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void disableAll() {
        List<String> modulesToDisable = new ArrayList<>(enabledModules);
        Collections.reverse(modulesToDisable); // Disable in reverse order of enabling to respect dependencies

        for (String moduleName : modulesToDisable) {
            Module module = loadedModules.get(moduleName);
            if (module != null) {
                module.onDisable();
                enabledModules.remove(moduleName);
                System.out.println("Disabled module: " + moduleName);
            }
        }

        System.out.println("Successfully disabled all modules.");
    }

    private static void deleteFile(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + file);
            e.printStackTrace();
        }
    }

    private static void enableModule(CoreProvider<?> provider, String moduleName, Path modulesPath) {
        if (enabledModules.contains(moduleName)) {
            return; // Module already enabled
        }

        List<String> dependencies = moduleDependencies.get(moduleName);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                if (!enabledModules.contains(dependency)) {
                    enableModule(provider, dependency, modulesPath);
                }
            }
        }

          Module module = loadedModules.get(moduleName);
        if (module != null) {
            Path modulePath = modulesPath.resolve(moduleName);
            try {
                // Create a directory for the module if it doesn't exist
                if (Files.notExists(modulePath)) {
                    Files.createDirectories(modulePath);
                }
            } catch (IOException e) {
                System.err.println("Failed to create directory for module: " + moduleName);
                e.printStackTrace();
            }

            module.onEnable(provider);
            enabledModules.add(moduleName);
            System.out.println("Enabled module: " + moduleName);
        }
    }
}
