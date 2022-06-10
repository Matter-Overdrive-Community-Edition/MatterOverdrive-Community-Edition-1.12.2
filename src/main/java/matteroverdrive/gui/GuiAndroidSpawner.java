
package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerAndroidSpawner;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.MOElementBase;
import matteroverdrive.gui.element.MOElementButtonScaled;
import matteroverdrive.tile.TileEntityAndroidSpawner;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAndroidSpawner extends MOGuiMachine<TileEntityAndroidSpawner> {
	private MOElementButtonScaled deleteAllBt;

	public GuiAndroidSpawner(InventoryPlayer inventoryPlayer, TileEntityAndroidSpawner machine) {
		super(new ContainerAndroidSpawner(inventoryPlayer, machine), machine);
		deleteAllBt = new MOElementButtonScaled(this, this, 64, 32 + 28, "delete_all", 60, 20);
		deleteAllBt.setText("KIll All");
	}

	@Override
	public void initGui() {
		super.initGui();
		pages.get(0).addElement(deleteAllBt);

		for (int i = 1; i < TileEntityAndroidSpawner.FLASH_DRIVE_COUNT; i++) {
			ElementInventorySlot flashDriveSlot = new ElementInventorySlot(this, (MOSlot) inventorySlots.getSlot(i),
					60 + 24 * (i - 1), 32, 22, 22, "big");
			pages.get(0).addElement(flashDriveSlot);
		}
		AddMainPlayerSlots(inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(inventorySlots, this);
	}

	@Override
	public void handleElementButtonClick(MOElementBase element, String elementName, int mouseButton) {
		super.handleElementButtonClick(element, elementName, mouseButton);
		if (element == deleteAllBt) {
			mc.playerController.sendEnchantPacket(getContainer().windowId, 0);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (pages.get(0).isVisible()) {
			getFontRenderer().drawString(String.format("%s/%s",
					((ContainerAndroidSpawner) getContainer()).getSpawnedCount(), machine.getMaxSpawnCount()), 130, 68,
					Reference.COLOR_HOLO.getColor());
			int spawnDelay = machine.getSpawnDelay();
			if (spawnDelay > 0) {
				int spawnTime = spawnDelay - (int) (machine.getWorld().getTotalWorldTime() % spawnDelay);
				getFontRenderer().drawString(
						String.format("Time to next spawn: %s", MOStringHelper.formatRemainingTime(spawnTime / 20f)),
						54, 68 + 16, Reference.COLOR_HOLO.getColor());
			}
		}
	}
}
