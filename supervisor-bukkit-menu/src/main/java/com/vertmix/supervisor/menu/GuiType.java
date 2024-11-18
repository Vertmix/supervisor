package com.vertmix.supervisor.menu;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {

    CHEST(InventoryType.CHEST, 9),
    WORKBENCH(InventoryType.WORKBENCH, 9),
    HOPPER(InventoryType.HOPPER, 5),
    DISPENSER(InventoryType.DISPENSER, 8),
    BREWING(InventoryType.BREWING, 4);

    @NotNull
    private final InventoryType inventoryType;
    private final int limit;

    GuiType(@NotNull final InventoryType inventoryType, final int limit) {
        this.inventoryType = inventoryType;
        this.limit = limit;
    }

    @NotNull
    public InventoryType getInventoryType() {
        return inventoryType;
    }

}