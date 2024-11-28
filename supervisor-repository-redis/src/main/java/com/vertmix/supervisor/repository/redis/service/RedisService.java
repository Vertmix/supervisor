package com.vertmix.supervisor.repository.redis.service;

import com.vertmix.supervisor.repository.redis.DataProxyHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.vertmix.supervisor.repository.redis.DataProxyHandler.GSON;

public class RedisService {

    private static final Map<Class<?>, Consumer<?>> listeners = new ConcurrentHashMap<>();
    private static JedisPool jedisPool;

    /**
     * Initializes the RedisEventManager with the provided JedisPool.
     *
     * @param pool The Redis connection pool.
     */
    public static void initialize(JedisPool pool) {
        jedisPool = pool;
    }

    /**
     * Publishes an event to Redis.
     *
     * @param channel The Redis channel to publish to.
     * @param packet  The packet to publish.
     */
    public static void publish(String channel, Object packet) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, packet.toString());
        }
    }

    /**
     * Subscribes to a Redis channel for packet events.
     *
     * @param channel  The Redis channel to subscribe to.
     * @param listener The listener that processes incoming packets.
     */
    public static <T> void subscribe(String channel, Class<T> packetClass, Consumer<T> listener) {
        listeners.put(packetClass, listener);

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            T packet = deserialize(message, packetClass);
                            Consumer<T> consumer = (Consumer<T>) listeners.get(packetClass);
                            if (consumer != null) {
                                consumer.accept(packet);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, channel);
            }
        }).start();
    }

    /**
     * Deserializes a message into a packet object.
     *
     * @param message     The message to deserialize.
     * @param packetClass The class of the packet.
     * @param <T>         The type of the packet.
     * @return The deserialized packet.
     */
    private static <T> T deserialize(String message, Class<T> packetClass) {
        return GSON.fromJson(message, packetClass);
    }
}
