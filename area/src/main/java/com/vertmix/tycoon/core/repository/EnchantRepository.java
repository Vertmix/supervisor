package com.vertmix.tycoon.core.repository;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.repository.json.JsonRepository;
import com.vertmix.tycoon.core.model.EnchantData;

@Component
@Navigation(path = "/data/enchants/")
public interface EnchantRepository extends JsonRepository<EnchantData> {}
