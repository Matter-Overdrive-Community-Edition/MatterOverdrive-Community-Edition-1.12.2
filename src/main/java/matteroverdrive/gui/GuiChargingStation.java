
package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineChargingStation;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiChargingStation extends MOGuiMachine<TileEntityMachineChargingStation> {
	MOElementEnergy energy;

	public GuiChargingStation(InventoryPlayer inventoryPlayer, TileEntityMachineChargingStation chargingStation) {
		super(ContainerFactory.createMachineContainer(chargingStation, inventoryPlayer), chargingStation);
		name = "charging_station";
		energy = new MOElementEnergy(this, 80, 40, chargingStation.getEnergyStorage());
		energy.setTexture(Reference.TEXTURE_FE_METER, 32, 64);
	}

	@Override
	public void initGui() {
		super.initGui();
		pages.get(0).addElement(energy);
		AddMainPlayerSlots(inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(inventorySlots, this);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (pages.get(0).isVisible()) {
			fontRenderer.drawString(String.format("Range: %s", machine.getRange()), 100, 50,
					Reference.COLOR_HOLO.getColor());
			fontRenderer.drawString(String.format("Charge Rate: %s", machine.getMaxCharging()), 100, 62,
					Reference.COLOR_HOLO.getColor());
		}
	}
}
