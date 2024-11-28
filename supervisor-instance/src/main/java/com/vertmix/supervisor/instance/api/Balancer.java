package com.vertmix.supervisor.instance.api;

import java.util.Collection;

public interface Balancer<T> {

    T balance(Collection<T> collection);
}
