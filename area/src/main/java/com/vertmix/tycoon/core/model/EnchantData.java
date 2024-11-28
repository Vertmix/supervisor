package com.vertmix.tycoon.core.model;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.tycoon.core.api.rarity.RarityType;
import lombok.Data;

@Data
public class EnchantData {

    private final String id;
    private String title;
    private RarityType rarityType;

    private String description;

    private long price;

    private Icon icon;
}
