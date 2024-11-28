package com.vertmix.supervisor.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.ModulePriority;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.core.terminable.Terminal;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vertmix.supervisor.reflection.ReflectionUtil.getComponentConstructor;

public class SupervisorLoader {

    public static void register(CoreProvider<?> provider, Object... objects) {
        try {
            ClassLoader classLoader = provider.getClazz().getClassLoader();
//            System.out.println("PACKAGE NAME : " + packageName);

            // Configure Reflections
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(provider.getClazz().getPackageName(), classLoader))
                    .setScanners(new SubTypesScanner(false)));

            // Collect all classes in the package
            Set<Class<?>> classes = reflections.getSubTypesOf(Object.class).stream()
                    .filter(clazz -> clazz.getName().startsWith(provider.getClazz().getPackageName()))
                    .collect(Collectors.toSet());

            Services.clazzes.addAll(classes);

            // Process Modules
            Set<Module> modules = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (Module.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                    System.out.println("[INFO] Found module: " + clazz.getSimpleName());
                    modules.add(module);
                }
            }

            modules.stream().sorted(Comparator.comparingInt(m -> {
                return m.getClass().getAnnotation(ModulePriority.class) == null ? 1 : m.getClass().getAnnotation(ModulePriority.class).priority().getPriority();
            })).forEach(m -> m.onEnable(provider));

            for (Object o : objects) {
                Services.register(o.getClass(), o);
            }

            // Register all components
            Set<Class<?>> componentClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                Services.runClazzConsumer(clazz);
                if (process(clazz)) {
                    componentClasses.add(clazz);
                }
            }

            for (Class<?> componentClass : componentClasses) {
                try {
                    registerComponent(componentClass);
                } catch (Exception e) {
                    System.err.println("[ERROR] Error registering component: " + componentClass.getName());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error during registration process.");
            e.printStackTrace();
        }
    }

    public static void disable() {
        Services.getServices().values().stream()
                .filter(o -> o instanceof Terminal)
                .map(o -> (Terminal) o)
                .forEach(Terminal::closeSilently);
    }

    private static void registerComponent(Class<?> clazz) throws Exception {
        if (Services.getService(clazz) != null) {
            // Component is already registered
            return;
        }

        System.out.println("SEARCHING....");
        Services.getServices().forEach(((aClass, o) -> System.out.println("regist" + aClass.getSimpleName())));

        if (clazz.isInterface()) {
            System.out.println(clazz.getSimpleName());
            System.out.println(clazz.getInterfaces()[0]);
            // Handle interface by using factory from Services
            if (Services.getFactories().containsKey(clazz)) {
                System.out.println("CONTAINS");
                Services.register(clazz, Services.create(clazz));
            } else if (Services.getFactories().containsKey(clazz.getInterfaces()[0])) {
                Services.register(clazz, Services.create(clazz));
            } else {
                System.err.println("[WARNING] No factory found for interface: " + clazz.getName());
            }
            return;
        }

        Constructor<?> constructor = getComponentConstructor(clazz);
        if (constructor == null) {
            System.err.println("[WARNING] No valid constructor found for component: " + clazz.getName());
            return;
        }

        // Resolve constructor parameters
        Object[] params = resolveDependencies(constructor.getParameterTypes());

        // Create and register the component
        Object componentInstance = constructor.newInstance(params);
        Services.runConsumer(componentInstance);
        Services.register(clazz, componentInstance);
        System.out.println("EXAMPLE RUNNING? " + clazz.getSimpleName());
        System.out.println("[DEBUG] Registered component: " + clazz.getName());
    }

    private static Object[] resolveDependencies(Class<?>[] paramTypes) throws Exception {
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            Object serviceInstance = Services.getService(paramType);

            if (serviceInstance != null) {
                params[i] = serviceInstance;
            } else if (process(paramType)) {
                // Register dependency if not already registered
                registerComponent(paramType);
                params[i] = Services.getService(paramType);
            } else {
                throw new IllegalStateException("Unable to resolve parameter: " + paramType.getName());
            }
        }
        return params;
    }

    private static boolean process(Class<?> clazz) {
        for (Class<? extends Annotation> processor : Services.getProcessable()) {
            if (clazz.isAnnotationPresent(processor)) {
                return true;
            }
        }
        return false;
    }
}
