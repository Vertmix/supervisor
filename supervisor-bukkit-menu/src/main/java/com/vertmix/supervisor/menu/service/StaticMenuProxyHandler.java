package com.vertmix.supervisor.menu.service;

import com.vertmix.supervisor.menu.menu.Menu;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class StaticMenuProxyHandler extends AbstractProxyHandler<Menu> {

    private final SimpleMenu menu;


    public StaticMenuProxyHandler(Class<Menu> serviceInterface, File file) {
        super(serviceInterface, true);
        this.menu = new SimpleMenu(file);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) { // handle defaults
            return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }

        return this.serviceInterface.getMethod(method.getName(), method.getParameterTypes()).invoke(menu, args);

    }

}
