package com.vertmix.tycoon.core.service;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.vertmix.supervisor.core.annotation.Component;
import com.vertmix.tycoon.core.api.prize.Prize;
import com.vertmix.tycoon.core.api.prize.PrizeExecutor;
import com.vertmix.tycoon.core.model.Area;
import com.vertmix.tycoon.core.repository.AreaRepository;
import lombok.AllArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

@Component
@AllArgsConstructor
public class AreaService implements Listener {

    private final AreaRepository areaRepository;
    private final PrizeExecutor executor = null;

    public void saveArea(Area area) {
        areaRepository.save(area.getId(), area);
    }

    public boolean contains(String key) {
        return areaRepository.containsKey(key);
    }

    public void reset(Area area, List<Prize<?>> prizes, int amount) {
        Region region = area.getRegion();

        // Get bottom floor y-coordinate
        int bottomY = region.getMinimumPoint().getY();

        // Collect all bottom floor positions
        Set<BlockVector3> bottomFloorPositions = new HashSet<>();
        for (int x = region.getMinimumPoint().getX(); x <= region.getMaximumPoint().getX(); x++) {
            for (int z = region.getMinimumPoint().getZ(); z <= region.getMaximumPoint().getZ(); z++) {
                BlockVector3 position = BlockVector3.at(x, bottomY, z);

                // Check if the position is solid and the block above is air
                if (isFloor(region, position)) {
                    bottomFloorPositions.add(position);
                }
            }
        }

//        // Ensure we have enough positions
//        if (bottomFloorPositions.size() < amount) {
//            throw new IllegalArgumentException("Not enough floor space to place all prizes.");
//        }

        // Randomly select positions for the prizes
        List<BlockVector3> positions = new ArrayList<>(bottomFloorPositions);
        Collections.shuffle(positions);
        List<BlockVector3> selectedPositions = positions.subList(0, Math.min(amount, prizes.size()));

        // Place prizes
        for (int i = 0; i < selectedPositions.size(); i++) {
            BlockVector3 position = selectedPositions.get(i);
            Prize<?> prize = prizes.get(i);

            placePrize(region, position, prize);
        }


    }

    private boolean isFloor(Region region, BlockVector3 position) {
        // Check if this position is the bottom-most solid block in the region
        BlockVector3 below = position.add(Direction.DOWN.toBlockVector());
        BlockVector3 above = position.add(Direction.UP.toBlockVector());
        return region.contains(position) &&
                isSolidBlock(region, position) &&
                !isSolidBlock(region, below) &&
                isAirBlock(region, above);
    }

    private boolean isSolidBlock(Region region, BlockVector3 position) {
        // Stub for solid block check (replace with your game-specific implementation)
        return true; // Assume all blocks are solid for now
    }

    private boolean isAirBlock(Region region, BlockVector3 position) {
        // Stub for air block check (replace with your game-specific implementation)
        return true; // Assume all non-solid blocks are air
    }

    private void placePrize(Region region, BlockVector3 position, Prize<?> prize) {
        // Stub for placing the prize in the game (replace with your implementation)
        System.out.println("Placed prize: " + prize + " at " + position);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();

    }
}
