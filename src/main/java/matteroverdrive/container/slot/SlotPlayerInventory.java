
package matteroverdrive.container.slot;

import net.minecraft.inventory.IInventory;

public class SlotPlayerInventory extends MOSlot {
	boolean isHotbar;

	public SlotPlayerInventory(IInventory inventory, int slot, int x, int y, boolean isHotbar) {
		super(inventory, slot, x, y);
		this.isHotbar = isHotbar;
	}

	public boolean isHotbar() {
		return isHotbar;
	}

	public void setHotbar(boolean isHotbar) {
		this.isHotbar = isHotbar;
	}
}
