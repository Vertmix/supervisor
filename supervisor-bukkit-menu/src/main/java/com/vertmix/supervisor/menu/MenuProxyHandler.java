package com.vertmix.supervisor.menu;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.reflection.AbstractProxyHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuProxyHandler extends AbstractProxyHandler<Menu> {

    private final SimpleMenu menu;

    public MenuProxyHandler(Class<Menu> serviceInterface, File file) {
        super(serviceInterface, true);
        this.menu = new SimpleMenu(file, serviceInterface);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "options":
                return menu.getOptions();
            case "set":
                char character = (char) args[0];
                Icon icon = (Icon) args[1];
                GuiAction<InventoryClickEvent> action = (GuiAction<InventoryClickEvent>) args[2];
                menu.set(character, icon, action);
                return null;
            case "render":
                menu.render();
                return null;
            case "setup":
                if (method.isDefault()) {
                    // Call the default method using privateLookupIn
                    return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                            .unreflectSpecial(method, method.getDeclaringClass())
                            .bindTo(proxy)
                            .invokeWithArguments(args);
                } else {
                    System.out.println("Could not find");
                    // Fallback to the SimpleMenu implementation if no default method
                    menu.setup();
                    return null;
                }
            case "init":
                menu.init();;
                return null;
            case "schema":
                return menu.getSchema();
            case "open":
                Player player = (Player) args[0];
                menu.open(player);
                return null;
            default:
                // Unsupported operations
                throw new UnsupportedOperationException("Unsupported operation: " + method.getName());
        }
    }

}
