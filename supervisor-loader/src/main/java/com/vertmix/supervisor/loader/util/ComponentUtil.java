package com.vertmix.supervisor.loader.util;

import com.vertmix.supervisor.core.service.Services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.vertmix.supervisor.reflection.ReflectionUtil.getComponentConstructor;

public class ComponentUtil {

    public static void registerComponents(Collection<Class<?>> classes) {
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
<<<<<<< HEAD
}
=======
}
>>>>>>> feature/loader
