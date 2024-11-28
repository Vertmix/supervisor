package com.vertmix.supervisor.economy.api;

import java.math.BigDecimal;

public interface Economy<T> {

    String getId();

    String getPrefix();

    boolean has(T player, BigDecimal amount);

    BigDecimal withdraw(T player, BigDecimal amount);

    BigDecimal deposit(T player, BigDecimal amount);
}
