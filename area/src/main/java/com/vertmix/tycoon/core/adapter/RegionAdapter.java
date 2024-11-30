//package com.vertmix.tycoon.core.adapter;
//
//import com.google.gson.*;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//import com.sk89q.worldedit.bukkit.BukkitAdapter;
//import com.sk89q.worldedit.bukkit.BukkitWorld;
//import com.sk89q.worldedit.math.BlockVector3;
//import com.sk89q.worldedit.regions.CuboidRegion;
//import com.vertmix.supervisor.adapter.Adapter;
//import org.bukkit.Bukkit;
//import org.bukkit.World;
//
//import java.io.IOException;
//
//@Adapter
//public class RegionAdapter extends TypeAdapter<CuboidRegion> {
//
//    @Override
//    public void write(JsonWriter out, CuboidRegion value) throws IOException {
//        if (value == null) {
//            out.nullValue();
//            return;
//        }
//
//        if (!(value instanceof CuboidRegion)) {
//            throw new IOException("Only CuboidRegion types are supported for serialization.");
//        }
//
//        // Begin writing the region object
//        out.beginObject();
//        out.name("worldName").value(value.getWorld().getName());
//
//        // Write minimum point (minPoint)
//        BlockVector3 minPoint = value.getMinimumPoint();
//        out.name("minPoint").beginObject();
//        out.name("x").value(minPoint.getX());
//        out.name("y").value(minPoint.getY());
//        out.name("z").value(minPoint.getZ());
//        out.endObject();
//
//        // Write maximum point (maxPoint)
//        BlockVector3 maxPoint = value.getMaximumPoint();
//        out.name("maxPoint").beginObject();
//        out.name("x").value(maxPoint.getX());
//        out.name("y").value(maxPoint.getY());
//        out.name("z").value(maxPoint.getZ());
//        out.endObject();
//
//        // End writing the region object
//        out.endObject();
//    }
//
//    @Override
//    public CuboidRegion read(JsonReader in) throws IOException {
//        String worldName = null;
//        BlockVector3 minPoint = null;
//        BlockVector3 maxPoint = null;
//
//        in.beginObject();
//        while (in.hasNext()) {
//            String name = in.nextName();
//            switch (name) {
//                case "worldName":
//                    worldName = in.nextString();
//                    break;
//                case "minPoint":
//                    minPoint = readPoint(in);
//                    break;
//                case "maxPoint":
//                    maxPoint = readPoint(in);
//                    break;
//                default:
//                    throw new JsonParseException("Unexpected field: " + name);
//            }
//        }
//        in.endObject();
//
//        if (worldName == null || minPoint == null || maxPoint == null) {
//            throw new JsonParseException("Missing required fields for Region deserialization");
//        }
//
//        World bukkitWorld = Bukkit.getWorld(worldName);
//        if (bukkitWorld == null) {
//            throw new JsonParseException("World \"" + worldName + "\" is not loaded or does not exist.");
//        }
//
//        return new CuboidRegion(BukkitAdapter.adapt(bukkitWorld), minPoint, maxPoint);
//    }
//
//    private BlockVector3 readPoint(JsonReader in) throws IOException {
//        int x = 0, y = 0, z = 0;
//
//        in.beginObject();
//        while (in.hasNext()) {
//            switch (in.nextName()) {
//                case "x":
//                    x = in.nextInt();
//                    break;
//                case "y":
//                    y = in.nextInt();
//                    break;
//                case "z":
//                    z = in.nextInt();
//                    break;
//                default:
//                    throw new JsonParseException("Unexpected field in point object");
//            }
//        }
//        in.endObject();
//
//        return BlockVector3.at(x, y, z);
//    }
//}
