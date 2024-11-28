package com.vertmix.supervisor.mongo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MongoContext {

    String collection();

    String database();
}
