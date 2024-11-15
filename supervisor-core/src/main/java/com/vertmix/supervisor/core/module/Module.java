package com.vertmix.supervisor.core.module;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.terminable.Terminal;

public interface Module<T> extends Terminal {
    void onEnable(CoreProvider<T> provider);

    void onDisable();

    @Override
    default void close() {
        onDisable();
    }
}
