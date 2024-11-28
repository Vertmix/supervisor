package com.vertmix.tycoon.core.controller;

import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.tycoon.core.model.Area;
import com.vertmix.tycoon.core.service.AreaService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AreaController {

    private final AreaService areaService;

    public boolean createArea(Area area) {
        if (areaService.contains(area.getId())) {
            return false;
        }
        areaService.saveArea(area);
        return true;
    }
}
