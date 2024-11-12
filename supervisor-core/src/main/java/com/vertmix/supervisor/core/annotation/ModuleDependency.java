package com.vertmix.supervisor.core.annotation;

import com.vertmix.supervisor.core.module.Module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDependency {

    Class<? extends Module> clazz();
}
