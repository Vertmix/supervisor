package com.vertmix.supervisor.menu.listener;

import com.vertmix.supervisor.menu.api.GuiAction;
import com.vertmix.supervisor.menu.service.AbstractMenu;
import com.vertmix.supervisor.menu.service.SimpleMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Optional;

public class MenuListener implements Listener {

    @EventHandler
    public void onGuiClick(final InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractMenu menu)) return;


        if (event.getClickedInventory() == null) {
            // Handles if the click happened outside both menus
            Optional.ofNullable(menu.getOutsideClickAction()).ifPresent(action -> action.run(event));

            return;
        }

        // Default Actions (for both player and custom gui)
        Optional.ofNullable(menu.getPlayerInventoryAction()).ifPresent(action -> action.run(event));

        // Default Actions (for both player and custom gui)
        Optional.ofNullable(menu.getDefaultClickAction()).ifPresent(action -> action.run(event));

        if (event.getClickedInventory().getType() == InventoryType.PLAYER)
            return; // After this point none of the below have anything to do with the player's inventory (bottom)

        // Default Top
        Optional.ofNullable(menu.getDefaultTopClickAction()).ifPresent(action -> action.run(event));

        // Slot Action (has priority over char actions)
        Optional.ofNullable(menu.getSlotAction(event.getSlot())).ifPresent(action -> action.run(event));

        // Char Action
        Optional.ofNullable(menu.getCharAction(event.getSlot())).ifPresent(action -> action.run(event));
    }

    @EventHandler
    public void onGuiDrag(final InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractMenu menu)) return;

        final GuiAction<InventoryDragEvent> dragAction = menu.getDragAction();
        if (dragAction != null) dragAction.run(event);
    }

//    @EventHandler
//    public void onGuiClose(final InventoryCloseEvent event) {
//        if (!(event.getInventory().getHolder() instanceof SimpleMenu menu)) return;
//
//        if (menu.isUpdating()) return;
//
//        menu.closeSilently();
//
//        final GuiAction<InventoryCloseEvent> closeAction = menu.getCloseGuiAction();
//        if (closeAction != null && menu.isRunCloseAction()) closeAction.run(event);
//    }
//
//    @EventHandler
//    public void onGuiOpen(final InventoryOpenEvent event) {
//        if (!(event.getInventory().getHolder() instanceof SimpleMenu menu)) return;
//
//        if (menu.isUpdating()) return;
//
//        Optional.ofNullable(menu.getOpenGuiAction()).ifPresent(action -> action.run(event));
//    }
}
