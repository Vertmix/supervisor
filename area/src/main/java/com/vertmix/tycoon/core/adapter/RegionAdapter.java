package com.vertmix.tycoon.core.adapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.vertmix.supervisor.adapter.Adapter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

@Adapter
public class RegionAdapter extends TypeAdapter<CuboidRegion> {

    @Override
    public void write(JsonWriter out, CuboidRegion value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (!(value instanceof CuboidRegion)) {
            throw new IOException("Only CuboidRegion types are supported for serialization.");
        }

        CuboidRegion cuboidRegion = (CuboidRegion) value;

        // Begin writing the region
        out.beginObject();
        out.name("region").beginObject();
        out.name("worldName").value(cuboidRegion.getWorld().getName());

        // Write the minimum point
        BlockVector3 min = cuboidRegion.getMinimumPoint();
        out.name("min").beginObject();
        out.name("x").value(min.getX());
        out.name("y").value(min.getY());
        out.name("z").value(min.getZ());
        out.endObject();

        // Write the maximum point
        BlockVector3 max = cuboidRegion.getMaximumPoint();
        out.name("max").beginObject();
        out.name("x").value(max.getX());
        out.name("y").value(max.getY());
        out.name("z").value(max.getZ());
        out.endObject();

        // End writing the region
        out.endObject();
        out.endObject();
    }

    @Override
    public CuboidRegion read(JsonReader in) throws IOException {
        in.beginObject();
        String worldName = null;
        BlockVector3 min = null;
        BlockVector3 max = null;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "region":
                    in.beginObject();
                    while (in.hasNext()) {
                        String regionField = in.nextName();
                        switch (regionField) {
                            case "worldName":
                                worldName = in.nextString();
                                break;
                            case "min":
                                in.beginObject();
                                int minX = 0, minY = 0, minZ = 0;
                                while (in.hasNext()) {
                                    switch (in.nextName()) {
                                        case "x":
                                            minX = in.nextInt();
                                            break;
                                        case "y":
                                            minY = in.nextInt();
                                            break;
                                        case "z":
                                            minZ = in.nextInt();
                                            break;
                                    }
                                }
                                in.endObject();
                                min = BlockVector3.at(minX, minY, minZ);
                                break;
                            case "max":
                                in.beginObject();
                                int maxX = 0, maxY = 0, maxZ = 0;
                                while (in.hasNext()) {
                                    switch (in.nextName()) {
                                        case "x":
                                            maxX = in.nextInt();
                                            break;
                                        case "y":
                                            maxY = in.nextInt();
                                            break;
                                        case "z":
                                            maxZ = in.nextInt();
                                            break;
                                    }
                                }
                                in.endObject();
                                max = BlockVector3.at(maxX, maxY, maxZ);
                                break;
                            default:
                                throw new JsonParseException("Unexpected field: " + regionField);
                        }
                    }
                    in.endObject();
                    break;
                default:
                    throw new JsonParseException("Unexpected field: " + name);
            }
        }
        in.endObject();

        if (worldName == null || min == null || max == null) {
            throw new JsonParseException("Missing field for Region deserialization");
        }

        World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) {
            throw new JsonParseException("World \"" + worldName + "\" is not loaded or does not exist.");
        }

        return new CuboidRegion(BukkitAdapter.adapt(bukkitWorld), min, max);
    }
}
