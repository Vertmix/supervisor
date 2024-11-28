package com.vertmix.supervisor.core;

import java.nio.file.Path;

public abstract class CoreProvider<T> {

    protected final Path path;
    protected final T source;

    protected boolean debug = false;

    public CoreProvider(Path path, T source) {
        this.path = path;
        this.source = source;
    }

    public Class<?> getClazz() {
        return this.source.getClass();
    }

    public Path getPath() {
        return path;
    }

    public T getSource() {
        return source;
    }

    public abstract void log(String str);

    public void debug(String str) {
        if (debug)
            log("[DEBUG] " + str);
    }

    public abstract void error(String str);
}
