
package matteroverdrive.container;

import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.InscriberSlot;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.tile.TileEntityInscriber;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerInscriber extends ContainerMachine<TileEntityInscriber> {
	public ContainerInscriber() {
		super();
	}

	public ContainerInscriber(InventoryPlayer inventory, TileEntityInscriber machine) {
		super(inventory, machine);
		onCraftMatrixChanged(machine);
	}

	@Override
	public void init(InventoryPlayer inventory) {
		Inventory machineInventory = machine.getInventoryContainer();
		for (matteroverdrive.data.inventory.Slot slot : machineInventory.getSlots()) {
			if (slot instanceof InscriberSlot) {
				addSlotToContainer(new InputSlot(machineInventory, slot, 0, 0));
			} else {
				addSlotToContainer(new SlotInventory(machineInventory, slot, 0, 0));
			}
		}
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		machine.calculateRecipe();
	}

	private class InputSlot extends SlotInventory {
		public InputSlot(IInventory inventory, Slot slot, int x, int y) {
			super(inventory, slot, x, y);
		}

		@Override
		public void onSlotChanged() {
			machine.calculateRecipe();
		}
	}
}
