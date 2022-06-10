
package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.ElementDualScaled;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineMatterRecycler;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiRecycler extends MOGuiMachine<TileEntityMachineMatterRecycler> {
	MOElementEnergy energyElement;
	ElementDualScaled recycle_progress;
	ElementSlot outputSlot;

	public GuiRecycler(InventoryPlayer inventoryPlayer, TileEntityMachineMatterRecycler machine) {
		super(ContainerFactory.createMachineContainer(machine, inventoryPlayer), machine);

		name = "recycler";
		energyElement = new MOElementEnergy(this, 100, 39, machine.getEnergyStorage());
		recycle_progress = new ElementDualScaled(this, 32, 54);
		outputSlot = new ElementInventorySlot(this, getContainer().getSlotAt(machine.OUTPUT_SLOT_ID), 64, 52, 22, 22,
				"big");

		recycle_progress.setMode(1);
		recycle_progress.setSize(24, 16);
		recycle_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
		energyElement.setTexture(Reference.TEXTURE_FE_METER, 32, 64);
	}

	@Override
	public void initGui() {
		super.initGui();

		pages.get(0).addElement(outputSlot);
		pages.get(0).addElement(energyElement);
		this.addElement(recycle_progress);

		AddMainPlayerSlots(this.inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(this.inventorySlots, this);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
		recycle_progress.setQuantity(Math.round(((ContainerMachine<?>) getContainer()).getProgress() * 24));
		manageRequirementsTooltips();
	}

	void manageRequirementsTooltips() {
		if (!machine.getStackInSlot(machine.INPUT_SLOT_ID).isEmpty()) {
			energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
			energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
		} else {
			energyElement.setEnergyRequired(0);
			energyElement.setEnergyRequiredPerTick(0);
		}
	}
}