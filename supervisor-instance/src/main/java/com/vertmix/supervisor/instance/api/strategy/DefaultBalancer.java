package com.vertmix.supervisor.instance.api.strategy;

import com.vertmix.supervisor.instance.api.Balancer;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.ToIntFunction;

public class DefaultBalancer<T> implements Balancer<T> {

    private final ToIntFunction<T> valueExtractor;

    public DefaultBalancer(ToIntFunction<T> valueExtractor) {
        this.valueExtractor = valueExtractor;
    }

    @Override
    public T balance(Collection<T> collection) {
        return collection.stream()
                .min(Comparator.comparingInt(valueExtractor))
                .orElseThrow(() -> new IllegalArgumentException("Collection is empty"));
    }
}
