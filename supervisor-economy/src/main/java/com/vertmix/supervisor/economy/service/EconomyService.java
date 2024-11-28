package com.vertmix.supervisor.economy.service;

import com.vertmix.supervisor.economy.api.Economy;
import com.vertmix.supervisor.economy.repository.EconomyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class EconomyService<T> {

    private final EconomyRepository economyRepository;

    public BigDecimal set(UUID uuid, BigDecimal bigDecimal) {
        return bigDecimal;
    }

    public BigDecimal get(UUID uuid) {
        return BigDecimal.ONE;
    }

}