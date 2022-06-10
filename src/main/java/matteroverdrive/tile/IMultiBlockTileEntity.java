
package matteroverdrive.tile;

import java.util.List;

import net.minecraft.util.math.BlockPos;

/**
 * Used by TileEntities belonging to blocks with bounding boxes greater than
 * 1m^3
 *
 * @author shadowfacts
 */
public interface IMultiBlockTileEntity {

	List<BlockPos> getBoundingBlocks();

}
