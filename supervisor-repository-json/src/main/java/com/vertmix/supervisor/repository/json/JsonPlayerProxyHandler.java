package com.vertmix.supervisor.repository.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.vertmix.supervisor.adapter.AdapterModule.GSON;


/**
 * The {@code JsonPlayerProxyHandler} class extends the {@code AbstractProxyHandler} to provide a JSON-based
 * storage mechanism for player data with in-memory caching and reflective method handling.
 *
 * @param <T> The type of player data managed by this handler.
 */
public class JsonPlayerProxyHandler<T> extends AbstractProxyHandler<T>  {

    private final File directory;
    private final Class<T> entityType;
    private final Map<String, T> cache = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code JsonPlayerProxyHandler} for managing player-specific JSON data.
     *
     * @param serviceInterface The service interface representing the repository.
     * @param directory        The directory where JSON files will be stored.
     */
    public JsonPlayerProxyHandler(Class<T> serviceInterface, File directory) {
        super(serviceInterface);

        if (!serviceInterface.isInterface()) {
            throw new IllegalArgumentException("The service interface must be an interface.");
        }

        // Ensure the interface is parameterized correctly
        Type genericInterface = serviceInterface.getGenericInterfaces()[0];
        if (genericInterface instanceof ParameterizedType parameterizedType) {
            Type typeArgument = parameterizedType.getActualTypeArguments()[0];

            // Check if the type argument is a concrete class
            if (typeArgument instanceof Class<?> clazz) {
                this.entityType = (Class<T>) clazz;
            } else if (typeArgument instanceof TypeVariable<?>) {
                throw new IllegalArgumentException("The generic type parameter must not be a type variable. Ensure a concrete class is specified.");
            } else {
                throw new IllegalArgumentException("Unsupported type argument: " + typeArgument.getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("The service interface must specify generic type arguments.");
        }

        this.directory = directory;

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "find":
                return find((String) args[0]);
            case "save":
                save((String) args[0], (T) args[1]);
                return args[1];
            case "delete":
                return delete((String) args[0]);
            case "containsKey":
                return containsKey((String) args[0]);
            case "values":
                return values();
            case "keys":
                return keys();
            case "cache":
                cache.put((String) args[0], (T) args[1]);
            case "invalidate":
                invalidate((String) args[0]);
                return null;
            case "invalidateAll":
                invalidateAll();
                return null;
            case "deleteAll":
                deleteAll();
                return null;
            case "saveAll":
                saveAll();
                return null;
            case "findAll":
                return findAll();
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + method.getName());
        }
    }

    public T find(String key) {
        return get(key);
    }

    public boolean delete(String key) {
        boolean existed = containsKey(key);
        deleteInternal(key);
        return existed;
    }

    public Collection<T> values() {
        return getAllValues();
    }

    public Set<String> keys() {
        return cache.keySet();
    }

    public void invalidate(String key) {
        cache.remove(key);
    }

    public void invalidateAll() {
        cache.clear();
    }

    public void deleteAll() {
        cache.clear();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    System.err.println("Failed to delete file: " + file.getName());
                }
            }
        }
    }

    public void saveAll() {
        cache.forEach(this::save);
    }

    public Collection<T> findAll() {
        return values();
    }

    private T get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        File file = new File(directory, key + ".json");
        if (!file.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            T data = GSON.fromJson(reader, entityType);
            if (data != null) {
                cache.put(key, data);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void save(String key, T value) {
        cache.put(key, value);

        File file = new File(directory, key + ".json");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            CompletableFuture.runAsync(() -> {
                try (FileWriter writer = new FileWriter(file)) {
                    GSON.toJson(value, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteInternal(String key) {
        cache.remove(key);

        File file = new File(directory, key + ".json");
        if (file.exists() && !file.delete()) {
            System.err.println("Failed to delete file for key: " + key);
        }
    }

    private boolean containsKey(String key) {
        if (cache.containsKey(key)) {
            return true;
        }

        File file = new File(directory, key + ".json");
        return file.exists();
    }

    private Collection<T> getAllValues() {
        Map<String, T> allValues = new HashMap<>(cache);

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                String key = file.getName().replace(".json", "");
                if (!cache.containsKey(key)) {
                    try (FileReader reader = new FileReader(file)) {
                        T value = GSON.fromJson(reader, entityType);
                        if (value != null) {
                            allValues.put(key, value);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return allValues.values();
    }
}