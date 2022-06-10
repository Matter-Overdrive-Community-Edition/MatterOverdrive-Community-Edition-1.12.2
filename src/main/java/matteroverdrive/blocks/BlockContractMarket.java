
package matteroverdrive.blocks;

import matteroverdrive.tile.TileEntityMachineContractMarket;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockContractMarket extends BlockMonitor<TileEntityMachineContractMarket> {
	public BlockContractMarket(Material material, String name) {
		super(material, name);
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		setBoundingBox(new AxisAlignedBB(0, 1, 0, 1, 11 / 16d, 1));
		setHasGui(true);
	}

	@Override
	public Class<TileEntityMachineContractMarket> getTileEntityClass() {
		return TileEntityMachineContractMarket.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityMachineContractMarket();
	}
}
