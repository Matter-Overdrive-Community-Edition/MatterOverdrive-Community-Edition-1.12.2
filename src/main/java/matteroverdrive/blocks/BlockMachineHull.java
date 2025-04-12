
package matteroverdrive.blocks;

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.blocks.includes.MOBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockMachineHull extends MOBlock implements IDismantleable {

	public BlockMachineHull(Material material, String name) {
		super(material, name);
		setHardness(30.0F);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops) {
		if (!returnDrops) {
			IBlockState state = world.getBlockState(pos);
			world.setBlockToAir(pos);
			dropBlockAsItem(world, pos, state, 0);
		}

		return null;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, BlockPos pos) {
		return true;
	}

}
