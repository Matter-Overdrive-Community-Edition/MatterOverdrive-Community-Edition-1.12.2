
package matteroverdrive.container;

import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFusionReactor extends ContainerMachine<TileEntityMachineFusionReactorController> {
	private int energyPerTick;

	public ContainerFusionReactor(InventoryPlayer inventory, TileEntityMachineFusionReactorController machine) {
		super(inventory, machine);
	}

	@Override
	public void init(InventoryPlayer inventory) {
		addAllSlotsFromInventory(machine.getInventoryContainer());
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
	}

	@Override
	public void addListener(IContainerListener icrafting) {
		super.addListener(icrafting);
		icrafting.sendWindowProperty(this, 1, this.machine.getEnergyPerTick());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener listener : this.listeners) {
			if (this.energyPerTick != this.machine.getEnergyPerTick()) {
				listener.sendWindowProperty(this, 1, this.machine.getEnergyPerTick());
			}
		}

		this.energyPerTick = this.machine.getEnergyPerTick();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue) {
		super.updateProgressBar(slot, newValue);
		if (slot == 1) {
			energyPerTick = newValue;
		}
	}

	public int getEnergyPerTick() {
		return energyPerTick;
	}
}
