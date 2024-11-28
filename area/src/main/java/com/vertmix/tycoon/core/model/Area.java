package com.vertmix.tycoon.core.model;

import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.Data;

@Data
public class Area {

     private final String id;
     private final String title;

     private CuboidRegion region;


}
