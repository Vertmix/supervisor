package com.vertmix.tycoon.core.api.prize;

import org.bukkit.entity.Entity;

public abstract class EntityPrize extends AbstractPrize<Entity> {

    public EntityPrize(double chance) {
        super(chance);
    }
}
