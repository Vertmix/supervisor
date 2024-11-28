package com.vertmix.supervisor.repository.json;

import com.google.gson.reflect.TypeToken;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.vertmix.supervisor.adapter.AdapterModule.GSON;

/**
 * The {@code JsonProxyHandler} class serves as both a proxy handler for repository interfaces
 * and a JSON-backed storage implementation. This class enables repositories to interact with
 * JSON-based data storage while also providing an in-memory caching mechanism for enhanced performance.
 *
 * @param <T> The type of entities managed by the repository.
 */
public class JsonProxyHandler<T> extends AbstractProxyHandler<T> {

    private final File file;
    private final Class<?> entityType;
    private Map<String, T> cache = new HashMap<>();

    /**
     * Constructs a new {@code JsonProxyHandler} instance for the specified service interface and JSON file.
     *
     * @param serviceInterface The repository interface that this handler will proxy. Must be an interface.
     * @param file The file where the repository data will be persisted. Must not be null.
     * @throws IllegalArgumentException if the service interface is not an interface, or if the generic type parameter is invalid.
     */
    public JsonProxyHandler(Class<T> serviceInterface, File file) {
        super(serviceInterface);

        if (!serviceInterface.isInterface()) {
            throw new IllegalArgumentException("The service interface must be an interface.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) serviceInterface.getGenericInterfaces()[0];
        this.entityType = (Class<T>) parameterizedType.getActualTypeArguments()[0];

        this.file = file;
        this.cache = loadFromFile();
    }


    /**
     * Handles method invocations on the proxy instance by routing them to the appropriate JSON-backed operations.
     *
     * @param proxy The proxy instance that the method was invoked on.
     * @param method The method being called.
     * @param args The arguments of the method call.
     * @return The result of the method invocation.
     * @throws Throwable if an error occurs during method execution.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "find":
                return cache.get((String) args[0]);
            case "save":
                cache.put((String) args[0], (T) args[1]);
                saveToFile();
                return cache.get((String) args[0]);
            case "delete":
                cache.remove((String) args[0]);
                saveToFile();
                return null;
            case "containsKey":
                return cache.containsKey((String) args[0]);
            case "values":
                return new ArrayList<>(cache.values());
            case "keys":
                return new ArrayList<>(cache.keySet());
            case "cache":
                cache.put((String) args[0], (T) args[1]);
                return null;
            case "invalidate":
                cache.remove((String) args[0]);
                return null;
            case "invalidateAll":
                cache.clear();
                return null;
            case "deleteAll":
                cache.clear();
                saveToFile();
                return null;
            case "saveAll":
                saveToFile();
                return null;
            case "findAll":
                return new ArrayList<>(loadFromFile().values());
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + method.getName());
        }
    }

    /**
     * Loads the repository data from the specified JSON file.
     *
     * @return A map containing the loaded repository data.
     */
    private Map<String, T> loadFromFile() {
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Type type = TypeToken.getParameterized(Map.class, String.class, entityType).getType();
            Map<String, T> loadedCache = GSON.fromJson(reader, type);
            return loadedCache != null ? new ConcurrentHashMap<>(loadedCache) : new ConcurrentHashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    /**
     * Saves the current state of the repository data to the specified JSON file.
     */
    private void saveToFile() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            CompletableFuture.runAsync(() -> {
                try (FileWriter writer = new FileWriter(file)) {
                    GSON.toJson(cache, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
