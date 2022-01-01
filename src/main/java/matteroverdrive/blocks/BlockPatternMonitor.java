
package matteroverdrive.blocks;

import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockPatternMonitor extends BlockMonitor<TileEntityMachinePatternMonitor> {
    public BlockPatternMonitor(Material material, String name) {
        super(material, name);
        setHasGui(true);
        setBoundingBox(new AxisAlignedBB(0, 1, 0, 1, 11 / 16d, 1));
		
    }

    @Override
    public Class<TileEntityMachinePatternMonitor> getTileEntityClass() {
        return TileEntityMachinePatternMonitor.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachinePatternMonitor();
    }
}
