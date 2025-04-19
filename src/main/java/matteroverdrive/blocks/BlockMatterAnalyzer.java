
package matteroverdrive.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
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

public class BlockMatterAnalyzer extends MOMatterEnergyStorageBlock<TileEntityMachineMatterAnalyzer> {
	private static boolean keepInventory;

	public BlockMatterAnalyzer(Material material, String name) {
		super(material, name, true, true);
		setHasRotation();
		setHardness(20.0F);
		setLightOpacity(2);
		this.setResistance(5.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_DIRECTION, EnumFacing.NORTH));
		this.setTranslationKey("matter_analyzer");
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

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(MatterOverdrive.BLOCKS.matter_analyzer);
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer_running.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer_running.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
		} else {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, iblockstate.getValue(PROPERTY_DIRECTION)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public Class<TileEntityMachineMatterAnalyzer> getTileEntityClass() {
		return TileEntityMachineMatterAnalyzer.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityMachineMatterAnalyzer();
	}
}
