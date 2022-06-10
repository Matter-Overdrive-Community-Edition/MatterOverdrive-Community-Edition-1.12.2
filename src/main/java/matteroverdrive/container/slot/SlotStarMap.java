
package matteroverdrive.container.slot;

import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotStarMap extends MOSlot {
	EntityPlayer player;
	TileEntityMachineStarMap starMap;

	public SlotStarMap(TileEntityMachineStarMap starMap, int slot, EntityPlayer player) {
		super(starMap, slot, 0, 0);
		this.player = player;
		this.starMap = starMap;
	}

	@Override
	public boolean isValid(ItemStack itemStack) {
		return starMap.isItemValidForSlot(getSlotIndex(), itemStack, player);
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack itemStack) {
		starMap.onItemPickup(player, itemStack);
		return super.onTake(player, itemStack);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return starMap.getPlanet() == null || starMap.getPlanet().isOwner(player);
	}

	@Override
	public void putStack(ItemStack itemStack) {
		starMap.onItemPlaced(itemStack);
		super.putStack(itemStack);
	}
}
