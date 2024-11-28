package com.vertmix.supervisor.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModulePriority;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.ServicePriority;
import com.vertmix.supervisor.core.service.Services;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

@ModulePriority(priority = ServicePriority.HIGH)
public class AdapterModule implements Module<Object> {

    private final GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();
    public static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    @Override
    public void onEnable(CoreProvider<Object> provider) {
        System.out.println("Testing???");
//        Services.registerProcess(Adapter.class);
        Services.clazzes.forEach(clazz -> {
            if (clazz.isAnnotationPresent(Adapter.class)) {
                try {
                    System.out.println("Is adapter class: " + clazz.getSimpleName());
                    Constructor<?> constructor = clazz.getConstructor(); // Correct usage
                    TypeAdapter<?> adapterInstance = (TypeAdapter<?>) constructor.newInstance();
                    System.out.println("???");
                    System.out.println(clazz.getSimpleName());
                    System.out.println(getGenericType(clazz).getSimpleName());
                    System.out.println(adapterInstance.getClass().getSimpleName());
                    gsonBuilder.registerTypeAdapter(getGenericType(clazz), adapterInstance);

                    Bukkit.getLogger().info("Registered adapter " + clazz.getSimpleName().replaceAll("Class", ""));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    System.err.println("Failed to instantiate adapter: " + clazz.getSimpleName());
                    e.printStackTrace();
                }
            }
        });


        GSON = gsonBuilder.create();
    }

    @Override
    public void onDisable() {

    }

    private static Class<?> getGenericType(Class<?> adapterClass) {
        return ((Class<?>) ((ParameterizedType) adapterClass.getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}
