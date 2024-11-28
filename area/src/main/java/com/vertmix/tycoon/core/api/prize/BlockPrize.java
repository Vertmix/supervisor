package com.vertmix.tycoon.core.api.prize;

import org.bukkit.block.Block;

public abstract class BlockPrize extends AbstractPrize<Block> {

    public BlockPrize(double chance) {
        super(chance);
    }
}
