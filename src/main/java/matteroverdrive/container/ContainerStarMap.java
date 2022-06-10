
package matteroverdrive.container;

import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerStarMap extends ContainerMachine<TileEntityMachineStarMap> {
	public ContainerStarMap() {
		super();
	}

	public ContainerStarMap(InventoryPlayer inventory, TileEntityMachineStarMap machine) {
		super(inventory, machine);
	}

	@Override
	protected void init(InventoryPlayer inventory) {

		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 270, true, true);
	}
}
