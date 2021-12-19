
package matteroverdrive.world.buildings;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public interface IMOWorldGenBuilding<T extends MOWorldGenBuilding.ImageGenWorker> {
    String getName();

    void generate(Random random, BlockPos pos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, int layer, int placeNotify, T worker);
}
