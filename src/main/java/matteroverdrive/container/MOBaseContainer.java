
package matteroverdrive.container;

import matteroverdrive.container.slot.MOSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;

public abstract class MOBaseContainer extends Container {
	public MOBaseContainer() {
	}

	public MOBaseContainer(InventoryPlayer inventoryPlayer) {
	}

	@Nonnull
	public Slot addSlotToContainer(Slot slot) {
		return super.addSlotToContainer(slot);
	}

	public MOSlot getSlotAt(int id) {
		return (MOSlot) inventorySlots.get(id);
	}
}
