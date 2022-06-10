
package matteroverdrive.data.inventory;

import matteroverdrive.items.Contract;
import net.minecraft.item.ItemStack;

public class SlotContract extends Slot {
	public SlotContract(boolean isMainSlot) {
		super(isMainSlot);
	}

	@Override
	public boolean isValidForSlot(ItemStack item) {
		return item.getItem() instanceof Contract;
	}
}
