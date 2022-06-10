
package matteroverdrive.util;

import java.util.Optional;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class TileUtils {
	public static <T> Optional<T> getTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
		return Optional.ofNullable(getNullableTileEntity(world, blockPos, tClass));
	}

	public static <T> T getNullableTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
		TileEntity tileEntity = world.getTileEntity(blockPos);
		return tClass.isInstance(tileEntity) ? tClass.cast(tileEntity) : null;
	}

}