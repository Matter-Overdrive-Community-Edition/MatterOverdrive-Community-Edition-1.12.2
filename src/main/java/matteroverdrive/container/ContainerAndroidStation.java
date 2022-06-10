
package matteroverdrive.container;

import matteroverdrive.Reference;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAndroidStation extends ContainerMachine<TileEntityAndroidStation> {
	public ContainerAndroidStation(InventoryPlayer playerInventory, TileEntityAndroidStation machine) {
		super(playerInventory, machine);
	}

	@Override
	protected void init(InventoryPlayer inventory) {
		AndroidPlayer android = MOPlayerCapabilityProvider.GetAndroidCapability(inventory.player);

		for (int i = 0; i < Reference.BIONIC_OTHER + 1; i++) {
			addSlotToContainer(new SlotInventory(android, android.getInventory().getSlot(i), 0, 0));
		}
		addSlotToContainer(new SlotEnergy(android.getInventory(), Reference.BIONIC_BATTERY, 8, 55));

		addUpgradeSlots(machine.getInventoryContainer());
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
	}
}
