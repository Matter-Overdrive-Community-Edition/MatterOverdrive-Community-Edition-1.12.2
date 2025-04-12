
package matteroverdrive.items;

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.api.wrench.IWrenchable;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class Wrench extends MOBaseItem {
	public Wrench(String name) {
		super(name);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		IBlockState state = world.getBlockState(pos);
		EnumActionResult result = EnumActionResult.PASS;

		if (!state.getBlock().isAir(state, world, pos)) {
			PlayerInteractEvent e = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side,
					new Vec3d(hitX, hitY, hitZ));
			if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY) {
				return EnumActionResult.FAIL;
			}

			if (player.isSneaking() && state.getBlock() instanceof IDismantleable
					&& ((IDismantleable) state.getBlock()).canDismantle(player, world, pos)) {
				if (!world.isRemote) {
					((IDismantleable) state.getBlock()).dismantleBlock(player, world, pos, false);
				}
				result = EnumActionResult.SUCCESS;
			}
			if (state.getBlock() instanceof IWrenchable && !world.isRemote) {
				result = ((IWrenchable) state.getBlock()).onWrenchHit(stack, player, world, pos, side, hitX, hitY, hitZ)
						? EnumActionResult.SUCCESS
						: EnumActionResult.PASS;
			} else if (!player.isSneaking() && state.getBlock().rotateBlock(world, pos, side)) {
				result = EnumActionResult.SUCCESS;
			}
		}
		if (result == EnumActionResult.SUCCESS)
			player.swingArm(hand);
		return result;
	}

	@Override
	public boolean hasDetails(ItemStack stack) {
		return true;
	}

	public void DisplayInfo(EntityPlayer player, String msg, TextFormatting formatting) {
		if (player != null && !msg.isEmpty()) {
			player.sendStatusMessage(new TextComponentString(formatting + msg), true);
		}
	}
}
