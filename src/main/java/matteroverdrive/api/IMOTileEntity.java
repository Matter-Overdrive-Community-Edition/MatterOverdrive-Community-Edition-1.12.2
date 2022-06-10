
package matteroverdrive.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Simeon
 * @since 3/6/2015 Implemented by all Matter overdrive Tile Entities
 */
public interface IMOTileEntity {
	void onAdded(World world, BlockPos pos, IBlockState state);

	void onPlaced(World world, EntityLivingBase entityLiving);

	void onDestroyed(World worldIn, BlockPos pos, IBlockState state);

	void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock);

	void writeToDropItem(ItemStack itemStack);

	void readFromPlaceItem(ItemStack itemStack);

}
