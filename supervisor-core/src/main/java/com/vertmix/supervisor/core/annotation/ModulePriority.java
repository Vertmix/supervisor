package com.vertmix.supervisor.core.annotation;

import com.vertmix.supervisor.core.service.ServicePriority;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModulePriority {

    ServicePriority priority() default ServicePriority.NORMAL;
}
