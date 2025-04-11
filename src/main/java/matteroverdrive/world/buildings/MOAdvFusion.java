package matteroverdrive.world.buildings;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.world.MOImageGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class MOAdvFusion extends MOWorldGenBuilding {
	private static final int MIN_DISTANCE_APART = 256;

	public MOAdvFusion(String name) {
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "advfusion.png"), 24, 24);
		setyOffset(5);
		addMapping(0x00fffc, MatterOverdrive.BLOCKS.decomposer);
		addMapping(0xffa200, MatterOverdrive.BLOCKS.machine_hull);
		addMapping(0xfff600, MatterOverdrive.BLOCKS.fusion_reactor_controller);
		addMapping(0xaccb00, MatterOverdrive.BLOCKS.fusion_reactor_controller);
		//bottom
		addMapping(0xec1c24, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		//north
		addMapping(0xb97a56, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		//east
		addMapping(0xb83dba, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		//south
		addMapping(0x0ed145, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		//west
		addMapping(0xffaec8, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		//top
		addMapping(0xffca18, MatterOverdrive.BLOCKS.gravitational_stabilizer);
		
		addMapping(0x80b956, MatterOverdrive.BLOCKS.fusion_reactor_coil);
		addMapping(0xe400ff, MatterOverdrive.BLOCKS.gravitational_anomaly);
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker) {
	}

	@Override
	public int getMetaFromColor(int color, Random random) {
		return 255 - getAlphaFromColor(color);
	}

	@Override
	public boolean isLocationValid(World world, BlockPos pos) {
		pos = new BlockPos(pos.getX(), Math.min(pos.getY() + 86, world.getHeight() - 18), pos.getZ());
		return world.getBlockState(pos).getBlock() == Blocks.AIR
				&& world.getBlockState(pos.add(layerWidth, 0, 0)) == Blocks.AIR
				&& world.getBlockState(pos.add(0, 0, layerHeight)) == Blocks.AIR
				&& world.getBlockState(pos.add(layerWidth, 0, layerHeight)) == Blocks.AIR
				&& world.getBlockState(pos.add(0, 16, 0)) == Blocks.AIR
				&& world.getBlockState(pos.add(layerWidth, 16, 0)) == Blocks.AIR
				&& world.getBlockState(pos.add(0, 16, layerHeight)) == Blocks.AIR
				&& world.getBlockState(pos.add(layerWidth, 16, layerHeight)) == Blocks.AIR;
	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos) {
		return false;
	}

	@Override
	public WorldGenBuildingWorker getNewWorkerInstance() {
		return new WorldGenBuildingWorker();
	}

	@Override
	public void onBlockPlace(World world, IBlockState state, BlockPos pos, Random random, int color,
			MOImageGen.ImageGenWorker worker) {
		if (state.getBlock() == MatterOverdrive.BLOCKS.fusion_reactor_controller) {
			if (colorsMatch(color, 0xfff600)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.NORTH), 3);
			} else if (colorsMatch(color, 0xaccb00)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.UP), 3);
			}
		}
		if (state.getBlock() == MatterOverdrive.BLOCKS.gravitational_stabilizer) {
			if (colorsMatch(color, 0xffca18)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.UP), 3);
			} else if (colorsMatch(color, 0xec1c24)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.DOWN), 3);
			} else if (colorsMatch(color, 0xb97a56)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.SOUTH), 3);
			} else if (colorsMatch(color, 0xb83dba)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.WEST), 3);
			} else if (colorsMatch(color, 0x0ed145)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.NORTH), 3);
			} else if (colorsMatch(color, 0xffaec8)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.EAST), 3);
			}
		}
	}
}