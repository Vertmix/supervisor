package com.vertmix.supervisor.menu.listener;

import com.vertmix.supervisor.menu.item.GuiAction;
import com.vertmix.supervisor.menu.service.SimpleMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class MenuListener implements Listener {

    @EventHandler
    public void onGuiClick(final InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof SimpleMenu menu)) return;

        final GuiAction<InventoryClickEvent> outsideClickAction = menu.getOutsideClickAction();
        if (outsideClickAction != null && event.getClickedInventory() == null) {
            outsideClickAction.run(event);
            return;
        }

        if (event.getClickedInventory() == null) return;

        final GuiAction<InventoryClickEvent> defaultTopClick = menu.getDefaultTopClickAction();
        if (defaultTopClick != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            defaultTopClick.run(event);
        }

        final GuiAction<InventoryClickEvent> playerInventoryClick = menu.getPlayerInventoryAction();
        if (playerInventoryClick != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            playerInventoryClick.run(event);
        }

        final GuiAction<InventoryClickEvent> defaultClick = menu.getDefaultClickAction();
        if (defaultClick != null) defaultClick.run(event);

        final GuiAction<InventoryClickEvent> slotAction = menu.getAction(event.getSlot());
        if (slotAction != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            slotAction.run(event);
        }

        final GuiAction<InventoryClickEvent> itemAction = menu.getAction(event.getSlot());
        if (itemAction != null) itemAction.run(event);
    }

    @EventHandler
    public void onGuiDrag(final InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof SimpleMenu menu)) return;

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
