package com.vertmix.tycoon.core.api.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractPrize<T> implements Prize<T> {

    private final double chance;

}
