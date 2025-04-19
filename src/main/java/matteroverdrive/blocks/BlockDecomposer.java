package matteroverdrive.blocks;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import javax.annotation.Nonnull;

public class BlockDecomposer extends MOMatterEnergyStorageBlock<TileEntityMachineDecomposer> {
	private static boolean keepInventory;

	public BlockDecomposer(Material material, String name) {
		super(material, name, true, true);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_DIRECTION, EnumFacing.NORTH));
		this.setTranslationKey("decomposer");
		setHasGui(true);
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY_DIRECTION);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (keepInventory) {

		} else {

			super.breakBlock(worldIn, pos, state);
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(MatterOverdrive.BLOCKS.decomposer);
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.decomposer_running.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.decomposer_running.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
		} else {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.decomposer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.decomposer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public Class<TileEntityMachineDecomposer> getTileEntityClass() {
		return TileEntityMachineDecomposer.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityMachineDecomposer();
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config) {
		super.onConfigChanged(config);
		config.initMachineCategory(getTranslationKey());
		TileEntityMachineDecomposer.MATTER_STORAGE = config.getMachineInt(getTranslationKey(), "storage.matter", 1024,
				String.format("How much matter can the %s hold", getLocalizedName()));
		TileEntityMachineDecomposer.ENERGY_CAPACITY = config.getMachineInt(getTranslationKey(), "storage.energy",
				512000, String.format("How much energy can the %s hold", getLocalizedName()));
		TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER = config.getMachineInt(getTranslationKey(),
				"speed.decompose", 80, "The speed in ticks, of decomposing. (per matter)");
		TileEntityMachineDecomposer.DECOMPOSE_ENERGY_PER_MATTER = config.getMachineInt(getTranslationKey(),
				"cost.decompose", 6000, "Decomposing cost per matter");

	}

}
