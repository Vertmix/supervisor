package com.vertmix.supervisor.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.vertmix.supervisor.repository.redis.DataProxyHandler;
import org.bson.Document;
import org.bson.conversions.Bson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MongoProxyHandler<T> extends DataProxyHandler<T> {

    private final MongoCollection<Document> collection;

    /**
     * Constructs a {@code MongoProxyHandler} for managing entity data in MongoDB with Redis caching.
     *
     * @param serviceInterface The service interface representing the repository.
     * @param database         The MongoDB database instance.
     * @param collectionName   The name of the MongoDB collection.
     * @param jedisPool        The Redis Jedis pool instance.
     */
    public MongoProxyHandler(Class<T> serviceInterface, MongoDatabase database, String collectionName, JedisPool jedisPool) {
        super(serviceInterface, jedisPool);
        this.collection = database.getCollection(collectionName);

        if (this.collection == null) {
            throw new IllegalArgumentException("The specified MongoDB collection does not exist: " + collectionName);
        }
    }

    @Override
    protected T find(String key) {
        T cachedEntity = getCache(key);
        if (cachedEntity != null) {
            return cachedEntity;
        }

        Bson filter = Filters.eq("_id", key);
        Document document = collection.find(filter).first();

        if (document == null) {
            return null;
        }

        T entity = documentToEntity(document);
        cacheToRedis(key, entity);
        return entity;
    }

    @Override
    protected void save(String key, T value) {
        cacheToRedis(key, value);

        Document document = entityToDocument(key, value);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        collection.replaceOne(Filters.eq("_id", key), document, options);
    }

    @Override
    protected void delete(String key) {
        invalidate(key);

        Bson filter = Filters.eq("_id", key);
        collection.deleteOne(filter);
    }

    @Override
    protected boolean containsKey(String key) {
        if (getCache(key) != null) {
            return true;
        }

        Bson filter = Filters.eq("_id", key);
        return collection.find(filter).iterator().hasNext();
    }

    @Override
    protected List<T> values() {
        List<T> allValues;

        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> cachedEntries = jedis.hgetAll(entityType.getSimpleName());
            allValues = new ArrayList<>(cachedEntries.values().stream().map(json -> GSON.fromJson(json, entityType)).toList());
        }

        for (Document document : collection.find()) {
            String key = document.getString("_id");
            T value = documentToEntity(document);
            allValues.add(value);
            cacheToRedis(key, value);
        }

        return allValues;
    }

    @Override
    protected List<String> keys() {
        List<String> keys = new ArrayList<>();

        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> cachedKeys = jedis.hkeys(entityType.getSimpleName());
            keys.addAll(cachedKeys);
        }

        for (Document document : collection.find()) {
            String key = document.getString("_id");
            if (!keys.contains(key)) {
                keys.add(key);
            }
        }

        return keys;
    }

    /**
     * Converts a MongoDB document into an entity object.
     *
     * @param document The MongoDB document.
     * @return The entity object.
     */
    private T documentToEntity(Document document) {
        try {
            String json = document.toJson();
            return GSON.fromJson(json, entityType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts an entity object into a MongoDB document.
     *
     * @param key   The unique key (ID) of the document.
     * @param value The entity object.
     * @return The MongoDB document.
     */
    private Document entityToDocument(String key, T value) {
        Document document = Document.parse(GSON.toJson(value));
        document.put("_id", key);
        return document;
    }
}
