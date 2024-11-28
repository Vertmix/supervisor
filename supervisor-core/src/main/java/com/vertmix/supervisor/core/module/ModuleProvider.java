package com.vertmix.supervisor.core.module;

import com.vertmix.supervisor.core.CoreProvider;

import java.nio.file.Path;

public abstract class ModuleProvider<T> extends CoreProvider<T> {

    public ModuleProvider(Path path, T type) {
        super(path, type);
    }


}
