package com.vertmix.supervisor.repository.mongo.bukkit;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleDependency;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;
import com.vertmix.supervisor.repository.mongo.MongoContext;
import com.vertmix.supervisor.repository.mongo.MongoModule;
import com.vertmix.supervisor.repository.mongo.MongoPlayerProxyHandler;
import org.bukkit.plugin.Plugin;

@ModuleDependency(clazz = MongoModule.class)
public class BukkitMongoModule implements Module<Plugin> {
    private MongoClient mongoClient;

    @Override
    public void onEnable(CoreProvider<Plugin> provider) {
        mongoClient = MongoClients.create("localhost");

        System.out.println("Enabled Mongo");
        Services.register(BukkitMongoPlayerRepository.class, clazz -> {
            MongoContext context = clazz.getAnnotation(MongoContext.class);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("dev");
            String collection = clazz.getSimpleName().replaceAll("repository", "");
            if (context != null) {
                mongoDatabase = mongoClient.getDatabase(context.database());
                collection = context.collection();
            }
            return new MongoPlayerProxyHandler<>(clazz, mongoDatabase, collection).getInstance();
        });


    }

    @Override
    public void onDisable() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }
}
