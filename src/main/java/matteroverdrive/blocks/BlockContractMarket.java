
package matteroverdrive.blocks;

import matteroverdrive.tile.TileEntityMachineContractMarket;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockContractMarket extends BlockMonitor<TileEntityMachineContractMarket> {
    public BlockContractMarket(Material material, String name) {
        super(material, name);
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
