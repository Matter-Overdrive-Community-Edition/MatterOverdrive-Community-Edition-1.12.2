
package matteroverdrive.tile;

import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Used by TileEntities belonging to blocks with bounding boxes greater than 1m^3
 *
 * @author shadowfacts
 */
public interface IMultiBlockTileEntity {

    List<BlockPos> getBoundingBlocks();

}
