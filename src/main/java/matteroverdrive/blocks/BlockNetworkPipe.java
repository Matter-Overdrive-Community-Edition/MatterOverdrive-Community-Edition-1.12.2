
package matteroverdrive.blocks;

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.tile.pipes.TileEntityNetworkPipe;
import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class BlockNetworkPipe extends BlockPipe<TileEntityNetworkPipe> implements IDismantleable {

	public BlockNetworkPipe(Material material, String name) {
		super(material, name);
		setHardness(10.0F);
		this.setResistance(9.0f);
	}

	@Override
	public Class<TileEntityNetworkPipe> getTileEntityClass() {
		return TileEntityNetworkPipe.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityNetworkPipe();
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops) {
		ArrayList<ItemStack> items = new ArrayList<>();
		IBlockState state = world.getBlockState(pos);

		if (!returnDrops) {
			state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
			state.getBlock().removedByPlayer(state, world, pos, player, true);
		} else {
			state.getBlock().removedByPlayer(state, world, pos, player, true);
			state.getBlock().breakBlock(world, pos, state);
			for (ItemStack itemStack : getDrops(world, pos, state, 0)) {
				MOInventoryHelper.insertItemStackIntoInventory(player.inventory, itemStack, EnumFacing.DOWN);
			}
		}

		return items;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, BlockPos pos) {
		return true;
	}
}
