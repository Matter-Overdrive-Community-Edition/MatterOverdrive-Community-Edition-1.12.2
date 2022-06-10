
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.gui.MOGuiBase;

import java.util.List;

public class ElementPlayerSlots extends MOElementBase {
	public ElementPlayerSlots(MOGuiBase gui, int posX, int posY) {
		super(gui, posX, posY);
		this.setTexture(Reference.PATH_ELEMENTS + "slot_bg.png", 18, 18);
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
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		gui.bindTexture(texture);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				drawTexturedModalRect(posX + j * 18, posY + i * 18, 0, 0, 18, 18);
			}
		}

		for (int i = 0; i < 9; i++) {
			drawTexturedModalRect(posX + i * 18, posY + 58, 0, 0, 18, 18);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}
}
