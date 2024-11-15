package com.vertmix.supervisor.loader;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.core.terminable.Terminal;
import com.vertmix.supervisor.repository.json.JsonProxyHandler;
import com.vertmix.supervisor.repository.json.JsonRepository;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vertmix.supervisor.reflection.ReflectionUtil.getComponentConstructor;

public class SupervisorLoader {


    public static void register(CoreProvider<?> provider) {
        try {
            String packageName = provider.getClass().getPackage().getName();
            ClassLoader classLoader = provider.getClass().getClassLoader();

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName, classLoader))
                    .setScanners(new SubTypesScanner(false)));

            // Collect all classes in the package
            final Set<Class<?>> classes = reflections.getAll(new SubTypesScanner(false)).stream().filter(x -> x.startsWith(packageName)).map(x -> {
                try {
                    return Class.forName(x);
                } catch (ClassNotFoundException e) {
                    System.out.println("Could not find class " + x);
                    return null;
                }
            }).collect(Collectors.toSet());

            Set<Module> modules = new HashSet<>();
            for (Class<?> clazz : classes) {
                System.out.println(clazz.getSimpleName());
                if (clazz.isInterface()) continue;
                if (clazz.getInterfaces().length == 0) continue;
                if (clazz.getInterfaces()[0] == Module.class) {
                    Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                    module.onEnable(provider);
                    modules.add(module);
                }
            }

            // First pass: register all components
            Set<Class<?>> componentClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (process(clazz))
                    componentClasses.add(clazz);

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
        Services.getServices().values().stream().filter(o -> o instanceof Terminal).map(o -> (Terminal)o).forEach(Terminal::closeSilently);
    }

    private static void registerComponent(Class<?> clazz) throws Exception {
        if (Services.getService(clazz) != null) {
            // Component is already registered
            return;
        }

        if (clazz.isInterface()) {
            // Handle interface by using factory from Services
            if (Services.getFactories().containsKey(clazz.getInterfaces()[0])) {
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
        // Preform mutations
        Services.runConsumer(componentInstance);
        // Register
        Services.register(clazz, componentInstance);
        System.out.println("[DEBUG] Registered component: " + clazz.getName());
    }

    private static Object[] resolveDependencies(Class<?>[] paramTypes) throws Exception {
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            Object serviceInstance = Services.getService(paramType);
            System.out.println(paramType.getSimpleName());
            System.out.println(process(paramType));

            if (serviceInstance != null) {
                params[i] = serviceInstance;
            } else if (process(paramType)) {
                System.out.println("can process");
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
            if (clazz.isAnnotationPresent(processor)){
                return true;
            }
        }
        return false;
    }
}
