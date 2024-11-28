package com.vertmix.tycoon.core.view.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.contexts.OnlinePlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.tycoon.core.controller.AreaController;
import com.vertmix.tycoon.core.model.Area;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Component
@CommandAlias("area")
@CommandPermission("vertmix.area.admin")
public class AreaCommand extends BaseCommand {

    private final AreaController areaController;
    private final WorldEditPlugin worldEditPlugin;

    public AreaCommand(AreaController areaController, PaperCommandManager commandManager) {
        this.areaController = areaController;
        this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        commandManager.registerCommand(this);
    }

    @Default
    @Description("Create a new area using your selected region.")
    public void onArea(Player player, @Single String id) {
        if (worldEditPlugin == null) {
            player.sendMessage("§cWorldEdit is not installed!");
            return;
        }

        LocalSession localSession = worldEditPlugin.getSession(player);
        Region region;

        try {
            region = localSession.getSelection();
        } catch (Exception e) {
            player.sendMessage("§cYou must select a valid region first!");
            return;
        }

        // Retrieve world
        World world = region.getWorld();
        if (world == null) {
            player.sendMessage("§cCould not determine the world of your selection.");
            return;
        }

        Area area = new Area(id, "Demo");
        area.setRegion(((CuboidRegion) region));

        if (areaController.createArea(area)) {
            player.sendMessage(String.format("§aArea '%s' created with bounds: %s", id, region.getBoundingBox().toString()));
        } else {
            player.sendMessage("§cThis area already exists!");
        }
    }
}
