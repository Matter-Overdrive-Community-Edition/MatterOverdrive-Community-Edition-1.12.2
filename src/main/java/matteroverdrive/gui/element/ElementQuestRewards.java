
package matteroverdrive.gui.element;

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ElementQuestRewards extends MOElementBase {
	List<ItemStack> itemStacks;

	public ElementQuestRewards(MOGuiBase gui, int posX, int posY, List<ItemStack> itemStacks) {
		super(gui, posX, posY);
		setItemStacks(itemStacks);
	}

	@Override
	public void updateInfo() {

	}

	@Override
	public void init() {

	}

	@Override
	public void addTooltip(List<String> var1, int mouseX, int mouseY) {

	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float v) {

	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		for (int i = 0; i < itemStacks.size(); i++) {
			RenderUtils.renderStack(posX + 18 * i, posY + 1, 0, itemStacks.get(i), true);
		}
	}

	public void setItemStacks(List<ItemStack> itemStacks) {
		this.itemStacks = itemStacks;
		if (itemStacks != null) {
			sizeY = 18;
			sizeX = 18 * itemStacks.size();
		}
	}
}
