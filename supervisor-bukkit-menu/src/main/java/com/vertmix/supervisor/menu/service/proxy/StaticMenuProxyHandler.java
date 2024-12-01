package com.vertmix.supervisor.menu.service.proxy;

import com.vertmix.supervisor.menu.api.Menu;
import com.vertmix.supervisor.menu.service.SimpleMenu;
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

            System.out.println("Simple default: [" + method.getName() + "]" + method.getDeclaringClass().getSimpleName());

            return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
        System.out.println("Simple: [" + method.getName() + "]" + method.getDeclaringClass().getSimpleName());

        return this.serviceInterface.getMethod(method.getName(), method.getParameterTypes()).invoke(menu, args);

    }

}