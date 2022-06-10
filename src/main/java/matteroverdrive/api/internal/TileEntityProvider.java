
package matteroverdrive.api.internal;

import javax.annotation.Nullable;

import matteroverdrive.util.TileUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface TileEntityProvider<T extends TileEntity> extends ITileEntityProvider {
	Class<T> getTileEntityClass();

	@Nullable
	@Override
	default T createNewTileEntity(World worldIn, int meta) {
		try {
			return getTileEntityClass().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	default T getTileEntity(IBlockAccess world, BlockPos pos) {
		return TileUtils.getNullableTileEntity(world, pos, getTileEntityClass());
	}
}