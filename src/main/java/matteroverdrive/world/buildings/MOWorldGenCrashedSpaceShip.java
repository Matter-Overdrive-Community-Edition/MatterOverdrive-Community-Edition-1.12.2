package matteroverdrive.world.buildings;

import java.util.Random;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.blocks.BlockTritaniumCrate;
import matteroverdrive.blocks.BlockWeaponStation;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.WeaponFactory;
import matteroverdrive.world.MOImageGen;
import matteroverdrive.world.MOLootTableManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class MOWorldGenCrashedSpaceShip extends MOWorldGenBuilding {
	private static final int MIN_DISTANCE_APART = 256;
	private final String[] holoTexts;

	public MOWorldGenCrashedSpaceShip(String name) {
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "crashed_ship.png"), 11, 35);
		holoTexts = new String[] { "Critical\nError", "Contacting\nSection 9", "System\nFailure",
				"Emergency\nPower\nOffline", "System\nReboot\nFailure", "Help Me", "I Need\nWater" };
		setyOffset(-1);
		addMapping(0x38c8df, MatterOverdrive.BLOCKS.decorative_clean);
		addMapping(0x187b8b, MatterOverdrive.BLOCKS.decorative_vent_bright);
		addMapping(0xaa38df, MatterOverdrive.BLOCKS.industrialGlass);
		addMapping(0x00ff78, Blocks.GRASS);
		addMapping(0xd8ff00, MatterOverdrive.BLOCKS.holoSign);
		addMapping(0xaccb00, MatterOverdrive.BLOCKS.holoSign);
		addMapping(0x3896df, MatterOverdrive.BLOCKS.decorative_tritanium_plate);
		addMapping(0xdfd938, MatterOverdrive.BLOCKS.decorative_tritanium_plate_stripe);
		addMapping(0x5d89ab, MatterOverdrive.BLOCKS.decorative_holo_matrix);
		addMapping(0x77147d, MatterOverdrive.BLOCKS.weapon_station);
		addMapping(0xb04a90, MatterOverdrive.BLOCKS.tritaniumCrateColored[EnumDyeColor.LIGHT_BLUE.getMetadata()]); // light
																													// blue
		addMapping(0x94deea, MatterOverdrive.BLOCKS.decorative_separator);
		addMapping(0xff9c00, MatterOverdrive.BLOCKS.decorative_coils);
		addMapping(0xaca847, MatterOverdrive.BLOCKS.decorative_matter_tube);
		addMapping(0x0c3b60, MatterOverdrive.BLOCKS.decorative_carbon_fiber_plate);
		addMapping(0xc5ced0, Blocks.AIR);
	}

	@Override
	public void onBlockPlace(World world, IBlockState state, BlockPos pos, Random random, int color,
			MOImageGen.ImageGenWorker worker) {
		if (state.getBlock() == MatterOverdrive.BLOCKS.holoSign) {
			if (colorsMatch(color, 0xd8ff00)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.EAST), 3);
			} else if (colorsMatch(color, 0xaccb00)) {
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.WEST), 3);
			}
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityHoloSign) {
				if (random.nextInt(100) < 30) {
					((TileEntityHoloSign) tileEntity).setText(holoTexts[random.nextInt(holoTexts.length)]);
				}
			}
		} else if (state.getBlock() instanceof BlockTritaniumCrate) {
			world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.WEST), 3);
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof IInventory) {
				TileEntityTritaniumCrate chest = (TileEntityTritaniumCrate) tileEntity;
				LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
				LootTable loottable = world.getLootTableManager()
						.getLootTableFromLocation(MOLootTableManager.MO_CRASHED_SHIP);
				loottable.fillInventory(chest, world.rand, lootcontext$builder.build());
				QuestStack questStack = MatterOverdrive.QUEST_FACTORY.generateQuestStack(random,
						MatterOverdrive.QUESTS.getQuestByName("crash_landing"));
				questStack.getTagCompound().setLong("pos", pos.toLong());
				MOInventoryHelper.insertItemStackIntoInventory((IInventory) tileEntity, questStack.getContract(),
						EnumFacing.DOWN);
			}

		} else if (state.getBlock() instanceof BlockWeaponStation) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityWeaponStation) {
				if (random.nextInt(200) < 10) {
					((TileEntityWeaponStation) tileEntity).setInventorySlotContents(
							((TileEntityWeaponStation) tileEntity).INPUT_SLOT,
							MatterOverdrive.WEAPON_FACTORY.getRandomDecoratedEnergyWeapon(
									new WeaponFactory.WeaponGenerationContext(3, null, true)));
				}
			}
		}
	}

	@Override
	public WorldGenBuildingWorker getNewWorkerInstance() {
		return new WorldGenBuildingWorker();
	}

	@Override
	public boolean isLocationValid(World world, BlockPos pos) {
		pos = new BlockPos(pos.getX(), Math.min(pos.getY(), world.getHeight()), pos.getZ());
		return world.getBlockState(pos).getBlock() == Blocks.GRASS
				&& world.getBlockState(pos.add(layerWidth, 0, 0)) == Blocks.GRASS
				&& world.getBlockState(pos.add(0, 0, layerHeight)) == Blocks.GRASS
				&& world.getBlockState(pos.add(layerWidth, 0, layerHeight)) == Blocks.GRASS
				&& world.getBlockState(pos.add(0, 16, 0)) == Blocks.GRASS
				&& world.getBlockState(pos.add(layerWidth, 16, 0)) == Blocks.GRASS
				&& world.getBlockState(pos.add(0, 16, layerHeight)) == Blocks.GRASS
				&& world.getBlockState(pos.add(layerWidth, 16, layerHeight)) == Blocks.GRASS;
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker) {

	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos) {
		return world.getBiome(pos) != Biome.REGISTRY.getObject(new ResourceLocation("minecraft", "ocean"))
				&& world.getBiome(pos) != Biome.REGISTRY.getObject(new ResourceLocation("minecraft", "frozen_ocean"))
				&& world.getBiome(pos) != Biome.REGISTRY.getObject(new ResourceLocation("minecraft", "deep_ocean"))
				&& isFarEnoughFromOthers(world, pos.getX(), pos.getZ(), MIN_DISTANCE_APART);

	}
}