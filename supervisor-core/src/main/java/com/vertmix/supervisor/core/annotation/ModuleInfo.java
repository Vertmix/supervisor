package com.vertmix.supervisor.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {
    String name();
    String version() default "0.0.1";
<<<<<<< HEAD
}
=======
}
>>>>>>> feature/loader
