
package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IFluidPipe extends IPipe<FluidPipeNetwork> {
	TileEntity getTile();

	boolean establishConnectionFromSide(IBlockState neighborState, EnumFacing opposite);
}
