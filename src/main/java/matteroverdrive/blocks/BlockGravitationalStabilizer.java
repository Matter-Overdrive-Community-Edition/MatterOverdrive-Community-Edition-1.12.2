
package matteroverdrive.blocks;

import javax.annotation.Nonnull;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityMachineGravitationalStabilizer;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGravitationalStabilizer extends MOBlockMachine<TileEntityMachineGravitationalStabilizer> {
	public BlockGravitationalStabilizer(Material material, String name) {
		super(material, name);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
		lightValue = 10;
		setRotationType(MOBlockHelper.RotationType.SIX_WAY);
	}

	/*
	 * @SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta) { if (side ==
	 * meta) { return MatterOverdriveIcons.Network_port_square; } else if (side ==
	 * MOBlockHelper.getOppositeSide(meta)) { return
	 * MatterOverdriveIcons.Monitor_back; } else if (side ==
	 * MOBlockHelper.getLeftSide(meta) || side == MOBlockHelper.getRightSide(meta))
	 * { return MatterOverdriveIcons.Vent2; }
	 * 
	 * return MatterOverdriveIcons.Coil; }
	 */

	@Override
	public Class<TileEntityMachineGravitationalStabilizer> getTileEntityClass() {
		return TileEntityMachineGravitationalStabilizer.class;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return (int) getTileEntity(worldIn, pos).getPercentage() * 15;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityMachineGravitationalStabilizer();
	}

}