package com.vertmix.supervisor.repository.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;
import com.vertmix.supervisor.repository.RepositoryCache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.vertmix.supervisor.adapter.AdapterModule.GSON;

/**
 * The {@code DataProxyHandler} class serves as an abstraction layer for repository interfaces
 * with support for multiple underlying storage implementations (e.g., MongoDB, SQL) and Redis caching.
 * This class enables repositories to interact with different types of data storage with enhanced performance.
 *
 * @param <T> The type of entities managed by the repository.
 */
public abstract class DataProxyHandler<T> extends AbstractProxyHandler<T>{
    protected final JedisPool jedisPool;
    protected final Class<T> entityType;

    /**
     * Constructs a {@code DataProxyHandler} for managing entity data with Redis caching.
     *
     * @param serviceInterface The service interface representing the repository.
     * @param jedisPool        The Redis Jedis pool instance.
     */
    public DataProxyHandler(Class<T> serviceInterface, JedisPool jedisPool) {
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
        this.jedisPool = jedisPool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "find":
                return find((String) args[0]);
            case "save":
                save((String) args[0], (T) args[1]);
                return null;
            case "delete":
                delete((String) args[0]);
                return null;
            case "containsKey":
                return containsKey((String) args[0]);
            case "values":
                return values();
            case "keys":
                return keys();
            case "cache":
                cache(args[0], (T) args[1]);
                return null;
            case "getCache":
                return getCache(args[0]);
            case "invalidate":
                invalidate(args[0]);
                return null;
            case "invalidateAll":
                invalidateAll();
                return null;
            case "saveCache":
                return saveCache();
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + method.getName());
        }
    }

    protected abstract T find(String key);

    protected abstract void save(String key, T value);

    protected abstract void delete(String key);

    protected abstract boolean containsKey(String key);

    protected abstract List<T> values();

    protected abstract List<String> keys();

    public void cache(Object key, T type) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(entityType.getSimpleName(), key.toString(), GSON.toJson(type)); // Use Redis Hash to store entity data
            jedis.expire(entityType.getSimpleName(), 43200); // Set 12-hour expiration for the hash
        }
    }

    public T getCache(Object key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String cachedValue = jedis.hget(entityType.getSimpleName(), key.toString());
            if (cachedValue != null) {
                return GSON.fromJson(cachedValue, entityType);
            }
        }
        return null;
    }

    public void invalidate(Object key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(entityType.getSimpleName(), key.toString());
        }
    }

    public void invalidateAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(entityType.getSimpleName());
        }
    }

    public Future<Map<String, T>> saveCache() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Map<String, String> cachedEntries = jedis.hgetAll(entityType.getSimpleName());
                return cachedEntries.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> GSON.fromJson(entry.getValue(), entityType)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Caches the entity to Redis with a 12-hour expiration.
     *
     * @param key    The unique key of the entity.
     * @param entity The entity to cache.
     */
    protected void cacheToRedis(String key, T entity) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(entityType.getSimpleName(), key, GSON.toJson(entity)); // Use Redis Hash to store entity data
            jedis.expire(entityType.getSimpleName(), 43200); // Set 12-hour expiration for the hash
        }
    }
}

