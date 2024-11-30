package com.vertmix.supervisor.menu.service.proxy;

import com.vertmix.supervisor.menu.api.PagedMenu;
import com.vertmix.supervisor.menu.service.SimplePagedMenu;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class PagedMenuProxyHandler extends AbstractProxyHandler<PagedMenu> {

    private final SimplePagedMenu menu;

    public PagedMenuProxyHandler(Class<PagedMenu> serviceInterface, File file) {
        super(serviceInterface, true);
        this.menu = new SimplePagedMenu(file);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) { // handle defaults
            return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }

//        System.out.println(method.getName());
        return this.serviceInterface.getMethod(method.getName(), method.getParameterTypes()).invoke(menu, args);

    }

}