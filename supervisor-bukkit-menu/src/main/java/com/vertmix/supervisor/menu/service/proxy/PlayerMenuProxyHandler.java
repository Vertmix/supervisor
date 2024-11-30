package com.vertmix.supervisor.menu.service.proxy;

import com.vertmix.supervisor.menu.api.PlayerMenu;
import com.vertmix.supervisor.menu.service.SimplePlayerMenu;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerMenuProxyHandler extends AbstractProxyHandler<PlayerMenu> {

    private static final ThreadLocal<SimplePlayerMenu> currentMenu = new ThreadLocal<>();
    private final File file;
    private final Map<UUID, SimplePlayerMenu> menuMap;

    public PlayerMenuProxyHandler(Class<PlayerMenu> serviceInterface, File file) {
        super(serviceInterface, true);

        this.menuMap = new HashMap<>();
        this.file = file;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("open") && args[0] instanceof Player player) {
            final SimplePlayerMenu simplePlayerMenu = new SimplePlayerMenu(file);

            currentMenu.set(simplePlayerMenu);

            simplePlayerMenu.init();
            simplePlayerMenu.render();

            currentMenu.remove();

            menuMap.put(player.getUniqueId(), simplePlayerMenu);

            simplePlayerMenu.open(player);
            return null;
        }

        if (method.isDefault()) { // handle defaults
            return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }

        if (args != null && args.length > 0 && method.getParameterTypes()[0] == Inventory.class && args[0] instanceof SimplePlayerMenu simplePlayerMenu) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?>[] remainingParameterTypes = Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);

            return this.serviceInterface.getMethod(method.getName(), remainingParameterTypes)
                    .invoke(simplePlayerMenu, Arrays.copyOfRange(args, 1, args.length));
        }

        if (args != null && args.length > 0 && args[0] instanceof Player player) {

            SimplePlayerMenu simplePlayerMenu = menuMap.get(player.getUniqueId());

            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?>[] remainingParameterTypes = Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);

            return this.serviceInterface.getMethod(method.getName(), remainingParameterTypes)
                    .invoke(simplePlayerMenu, Arrays.copyOfRange(args, 1, args.length));
        }

        if (currentMenu.get() != null) {
            return this.serviceInterface.getMethod(method.getName(), method.getParameterTypes())
                    .invoke(currentMenu.get(), args);
        }

        for (SimplePlayerMenu value : menuMap.values()) { // If not found trigger for all menus
            this.serviceInterface.getMethod(method.getName(), method.getParameterTypes())
                    .invoke(value, args);
        }

        return null;

    }


    private SimplePlayerMenu findMenuByInventory(Inventory inventory) {
        return menuMap.values().stream()
                .filter(menu -> menu.getInventory().equals(inventory))
                .findFirst()
                .orElse(null);
    }

}