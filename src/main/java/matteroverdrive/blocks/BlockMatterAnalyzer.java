
package matteroverdrive.blocks;

import javax.annotation.Nonnull;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMatterAnalyzer extends MOMatterEnergyStorageBlock<TileEntityMachineMatterAnalyzer> {
	public static final PropertyBool RUNNING = PropertyBool.create("running");

	public BlockMatterAnalyzer(Material material, String name) {
		super(material, name, true, true);
		setHasRotation();
		setHardness(20.0F);
		setLightOpacity(2);
		this.setResistance(5.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setDefaultState(getBlockState().getBaseState().withProperty(RUNNING, false)
				.withProperty(PROPERTY_DIRECTION, EnumFacing.NORTH));
		setHasGui(true);
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY_DIRECTION, RUNNING);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		IBlockState blockState = worldIn.getBlockState(pos);

		worldIn.setBlockState(pos, blockState.withProperty(RUNNING, false));
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (active) {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, state.getValue(PROPERTY_DIRECTION)).withProperty(RUNNING, true),
					3);
		} else {
			worldIn.setBlockState(pos, MatterOverdrive.BLOCKS.matter_analyzer.getDefaultState()
					.withProperty(PROPERTY_DIRECTION, state.getValue(PROPERTY_DIRECTION)).withProperty(RUNNING, false),
					3);
		}
		if (tileEntity != null) {
			tileEntity.validate();
			worldIn.setTileEntity(pos, tileEntity);
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
