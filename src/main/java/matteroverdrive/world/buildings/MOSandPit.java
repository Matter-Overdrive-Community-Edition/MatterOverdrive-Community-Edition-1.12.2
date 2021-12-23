
package matteroverdrive.world.buildings;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class MOSandPit extends MOWorldGenBuilding {
	private static final int MIN_DISTANCE_APART = 1024;
    private int airLeeway;

    public MOSandPit(String name, int airLeeway) {
        super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "sand_pit.png"), 24, 24);
        setMaxDistanceToAir(airLeeway);
        setyOffset(-9);
        validSpawnBlocks = new Block[]{Blocks.SAND};
        this.airLeeway = airLeeway;
        this.name = name;
        addMapping(0xe1db35, Blocks.SANDSTONE);
        addMapping(0xf1f1f1, Blocks.AIR);
        addMapping(0xffff00, Blocks.SAND);
        addMapping(0xc735e1, Blocks.GLOWSTONE);
        addMapping(0x35a2e1, Blocks.WATER);
        addMapping(0x359ae1, MatterOverdrive.BLOCKS.decorative_tritanium_plate);
        addMapping(0xff8400, MatterOverdrive.BLOCKS.decorative_coils);
        addMapping(0x6b4400, Blocks.OAK_FENCE);
    }

    public boolean isFlat(World world, BlockPos pos) {
        BlockPos y10 = world.getHeight(pos.add(layerWidth, 0, 0));
        BlockPos y11 = world.getHeight(pos.add(layerWidth, 0, layerHeight));
        BlockPos y01 = world.getHeight(pos.add(0, 0, layerHeight));
        if (Math.abs(pos.getY() - y10.getY()) <= airLeeway && Math.abs(pos.getY() - y11.getY()) <= airLeeway && Math.abs(pos.getY() - y01.getY()) <= airLeeway) {
            return blockBelowMatches(airLeeway, world, Blocks.SAND, pos) && blockBelowMatches(airLeeway, world, Blocks.SAND, pos.add(layerWidth, 0, 0)) && blockBelowMatches(airLeeway, world, Blocks.SAND, pos.add(0, 0, layerHeight)) && blockBelowMatches(airLeeway, world, Blocks.SAND, pos.add(layerWidth, 0, layerHeight));
        }
        return false;
    }

    private boolean blockBelowMatches(int airLeeway, World world, Block block, BlockPos pos) {
        for (int i = 0; i < airLeeway; i++) {
            if (world.getBlockState(pos.add(0, -i, 0)).getBlock() == block) {
                return true;
            }
        }
        return false;
    }

    private boolean isPointOnSurface(World world, BlockPos pos) {
        return world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR;
    }

    @Override
    public void placeBlock(World world, int color, BlockPos pos, int layer, Random random, int placeNotify, ImageGenWorker worker) {
        if ((color & 0xffffff) == 0xc735e1) {
            IBlockState block = getBlockFromColor(color, random);
            if (block != null) {
                world.setBlockState(pos, block, 3);
                onBlockPlace(world, block, pos, random, color, worker);
            }
        } else {
            super.placeBlock(world, color, pos, layer, random, placeNotify, worker);
        }
    }

    @Override
    public void onBlockPlace(World world, IBlockState state, BlockPos pos, Random random, int color, ImageGenWorker worker) {

    }

    @Override
    public WorldGenBuildingWorker getNewWorkerInstance() {
        return new WorldGenBuildingWorker();
    }

    @Override
    protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker) {

    }

    @Override
    public boolean shouldGenerate(Random random, World world, BlockPos pos) {
        return world.getBiome(pos) == Biome.REGISTRY.getObject(new ResourceLocation("minecraft", "desert"));
    }
}
