package com.vertmix.supervisor.module.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ModuleLoader {

    private static final Map<String, Module<?>> loadedModules = new LinkedHashMap<>();
    private static final Set<String> enabledModules = new LinkedHashSet<>();

    public static void register(CoreProvider<?> provider) {
        Path modulesPath = provider.getPath().resolve("modules");

        try {
            // Ensure the modules directory exists
            if (Files.notExists(modulesPath)) {
                Files.createDirectories(modulesPath);
            }

            System.out.println("Scanning module directory: " + modulesPath);

            // Clean up the directory: Remove non-JAR files
            try (Stream<Path> files = Files.list(modulesPath)) {
                files.filter(Files::isRegularFile)
                        .filter(file -> !file.toString().endsWith(".jar"))
                        .forEach(ModuleLoader::deleteFile);
            }

            // Load all JAR files
            try (Stream<Path> jarFiles = Files.list(modulesPath)) {
                List<Path> jarPaths = jarFiles.filter(file -> file.toString().endsWith(".jar")).toList();
                System.out.println("JAR files found: " + jarPaths);

                for (Path jarPath : jarPaths) {
                    loadModuleJar(provider, jarPath);
                }
            }

            // Enable modules
            for (String moduleName : loadedModules.keySet()) {
                enableModule(provider, moduleName);
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
            Module<?> module = loadedModules.get(moduleName);
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

    private static void loadModuleJar(CoreProvider<?> provider, Path jarPath) {
        try (JarFile jarFile = new JarFile(jarPath.toFile());
             URLClassLoader classLoader = new URLClassLoader(
                     new URL[]{jarPath.toUri().toURL()},
                     ModuleLoader.class.getClassLoader())) {


            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.endsWith(".class")  && name.contains("demo") && !name.contains("module-info")) {
                    String className = name.replace('/', '.').replace(".class", "");
                    System.out.println("Attempting to load class: " + className);

                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        Module<?> module = (Module<?>) clazz.getDeclaredConstructor().newInstance();
                        loadedModules.put("demo", module);
//                        if (clazz.isAnnotationPresent(ModuleInfo.class) && Module.class.isAssignableFrom(clazz)) {
//                            Module<?> module = (Module<?>) clazz.getDeclaredConstructor().newInstance();
//                            ModuleInfo moduleInfo = clazz.getAnnotation(ModuleInfo.class);
//
//                            System.out.println("Module detected: " + moduleInfo.name());
//
//                            loadedModules.put(moduleInfo.name(), module);
//                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load class: " + className);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read JAR file: " + jarPath);
            e.printStackTrace();
        }
    }

    private static void enableModule(CoreProvider provider, String moduleName) {
        if (enabledModules.contains(moduleName)) {
            return; // Module already enabled
        }

        Module<?> module = loadedModules.get(moduleName);
        if (module != null) {
            module.onEnable(provider);
            enabledModules.add(moduleName);
            System.out.println("Enabled module: " + moduleName);
        }
    }
}
