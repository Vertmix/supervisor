package com.vertmix.tycoon.core.repository;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.repository.json.JsonRepository;
import com.vertmix.tycoon.core.model.Area;

@Component
@Navigation(path = "/data/areas/")
public interface AreaRepository extends JsonRepository<Area> {}
