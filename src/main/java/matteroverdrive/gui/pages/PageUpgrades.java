
package matteroverdrive.gui.pages;

import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;
import matteroverdrive.client.data.Color;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.StackUtils;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUpgrades extends ElementBaseGroup {
	private Container container;
	private MOGuiMachine guiMachine;

	public PageUpgrades(MOGuiMachine gui, int posX, int posY, Container container) {
		this(gui, posX, posY, 0, 0, container);
	}

	public PageUpgrades(MOGuiMachine gui, int posX, int posY, int width, int height, Container container) {
		super(gui, posX, posY, width, height);
		this.container = container;
		this.guiMachine = gui;
	}

	@Override
	public void init() {
		super.init();
		AddUpgradeSlots(container, guiMachine.getMachine().getInventoryContainer());
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float ticks) {
		super.drawBackground(mouseX, mouseY, ticks);
		DrawUpgradeStats();
	}

	public void DrawUpgradeStats() {
		Map<UpgradeTypes, Double> upgradesMap = new HashMap<>();
		IUpgradeHandler upgradeHandler = guiMachine.getMachine().getUpgradeHandler();

		for (int i = 0; i < container.inventorySlots.size(); i++) {
			if (container.getSlot(i) instanceof SlotInventory
					&& ((SlotInventory) container.getSlot(i)).getSlot() instanceof UpgradeSlot) {
				ItemStack upgradeItem = container.getSlot(i).getStack();
				if (!StackUtils.isNullOrEmpty(upgradeItem) && MatterHelper.isUpgrade(upgradeItem)) {
					IUpgrade upgrade = (IUpgrade) upgradeItem.getItem();
					Map<UpgradeTypes, Double> upgradeMap = upgrade.getUpgrades(upgradeItem);
					for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet()) {
						if (guiMachine.getMachine().isAffectedByUpgrade(entry.getKey())) {
							double multiply = upgradeHandler != null
									? upgradeHandler.affectUpgrade(entry.getKey(), entry.getValue())
									: entry.getValue();
							if (upgradesMap.containsKey(entry.getKey())) {
								double previousValue = upgradesMap.get(entry.getKey());
								multiply = upgradeHandler.affectUpgrade(entry.getKey(),
										upgradeHandler != null
												? upgradeHandler.affectUpgrade(entry.getKey(), previousValue * multiply)
												: previousValue * multiply);
								upgradesMap.put(entry.getKey(), multiply);
							} else {
								upgradesMap.put(entry.getKey(), multiply);
							}
						}
					}
				}
			}
		}

		List<String> infos = new ArrayList<>();

		for (final Map.Entry<UpgradeTypes, Double> entry : upgradesMap.entrySet()) {
			if (guiMachine.getMachine().isAffectedByUpgrade(entry.getKey())) {
				if (entry.getKey() == UpgradeTypes.Muffler) {
					infos.add(TextFormatting.GREEN + "Muffling");
				} else {
					infos.add(MOStringHelper.toInfo(entry.getKey(), entry.getValue()));
				}
			}
		}

		RenderUtils.DrawMultilineInfo(infos, 76, 78, 100, 300, new Color(255, 255, 255).getColor());
	}

	public void AddUpgradeSlots(Container container, Inventory inventory) {
		int upgradeSlotIndex = 0;
		int upgradeCount = 0;
		for (int i = 0; i < container.inventorySlots.size(); i++) {
			if (container.getSlot(i) instanceof SlotInventory
					&& ((SlotInventory) container.getSlot(i)).getSlot() instanceof UpgradeSlot) {
				upgradeCount++;
			}
		}
		for (int i = 0; i < container.inventorySlots.size(); i++) {
			if (container.getSlot(i) instanceof SlotInventory
					&& ((SlotInventory) container.getSlot(i)).getSlot() instanceof UpgradeSlot) {
				ElementInventorySlot slotElement = new ElementInventorySlot(gui,
						(MOSlot) container.inventorySlots.get(i), 22, 22, "big");
				slotElement.setIcon(inventory.getSlot(container.inventorySlots.get(i).getSlotIndex()).getHoloIcon());
				int xStart = 12 + sizeX / 2 - MathHelper.clamp(upgradeCount * 24, 0, 5 * 24) / 2;
				slotElement.setPosition(xStart + (upgradeSlotIndex % 5) * 24, 52 + (upgradeSlotIndex / 5) * 24);
				this.addElement(slotElement);
				upgradeSlotIndex++;
			}
		}
	}
}
