package com.vertmix.supervisor.economy.controller;

import com.vertmix.supervisor.economy.service.EconomyService;

import java.math.BigDecimal;
import java.util.UUID;

public class EconomyController<T> {

    private final EconomyService economyService;

    public EconomyController(EconomyService economyService) {
        this.economyService = economyService;
    }

    public BigDecimal getBalance(UUID uuid) {
        return economyService.get(uuid);
    }

    public BigDecimal withdraw(UUID uuid, BigDecimal amount) {
        return amount;
    }

    public BigDecimal deposit(UUID uuid, BigDecimal amount) {
        return amount;
    }
}
