package com.vertmix.supervisor.repository;

public interface RepositoryCache<T> extends Repository<T> {

    void cache(Object key, T type);

    T getCache(Object key);

    void invalidate(Object key);

    void invalidateAll();

    void saveCache();
}
