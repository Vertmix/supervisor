package com.vertmix.supervisor.reflection;

import com.vertmix.supervisor.core.annotation.ComponentConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtil {

    public static Constructor<?> getComponentConstructor(Class<?> clazz) {
        if (clazz.isInterface()) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("No public constructors found for class: " + clazz.getName());
        }

        for (Constructor<?> clazzConstructor : constructors) {
            if (clazzConstructor.isAnnotationPresent(ComponentConstructor.class)) {
                return clazzConstructor;
            }
        }

        return constructors[0];
    }

    public static Collection<Class<?>> findClasses(String packageName, ClassLoader classLoader) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName, classLoader))
                .setScanners(new SubTypesScanner(false)));

        // Collect all classes in the package
       return reflections.getAll(new SubTypesScanner(false)).stream().filter(x -> x.startsWith(packageName)).map(x -> {
            try {
                return Class.forName(x);
            } catch (ClassNotFoundException e) {
                System.out.println("Could not find class " + x);
                return null;
            }
        }).collect(Collectors.toSet());

    }
}
