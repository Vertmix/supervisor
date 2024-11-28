package com.vertmix.supervisor.instance.bukkit.model;

import lombok.Data;
import org.bukkit.inventory.PlayerInventory;

@Data
public class SyncData {

    private PlayerInventory inventory;
    private double health;
    private double maxHealth;
    private int level;
    private double exp;

}
