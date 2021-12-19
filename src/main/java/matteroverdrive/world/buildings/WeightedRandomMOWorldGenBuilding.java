
package matteroverdrive.world.buildings;

import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WeightedRandomMOWorldGenBuilding extends WeightedRandom.Item {
    public final MOWorldGenBuilding worldGenBuilding;

    public WeightedRandomMOWorldGenBuilding(MOWorldGenBuilding worldGenBuilding, int weight) {
        super(weight);
        this.worldGenBuilding = worldGenBuilding;
    }

    public int getWeight(Random random, World world, BlockPos pos) {
        return worldGenBuilding.shouldGenerate(random, world, pos) && worldGenBuilding.isLocationValid(world, pos) ? itemWeight : Math.max(1, (int) (itemWeight * 1));
    }
}
