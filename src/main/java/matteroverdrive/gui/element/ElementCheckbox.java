
package matteroverdrive.gui.element;

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.proxy.ClientProxy;

public class ElementCheckbox extends MOElementButtonScaled {
	String checkboxLabel;
	boolean state;

	public ElementCheckbox(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, boolean state) {
		super(gui, handler, posX, posY, name, 16, 16);
		this.state = state;
		setNormalTexture(MOElementButtonScaled.HOVER_TEXTURE_DARK);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		getFontRenderer().drawString(checkboxLabel, posX + sizeX + 4,
				posY + sizeY / 2 - getFontRenderer().FONT_HEIGHT / 2, 0xFFFFFF);
		if (state) {
			ClientProxy.holoIcons.renderIcon("tick", posX + sizeX / 2 - 8, posY + sizeY / 2 - 8);
		}
	}

	public void setCheckboxLabel(String checkboxLabel) {
		this.checkboxLabel = checkboxLabel;
	}

	public void onAction(int mouseX, int mouseY, int mouseButton) {
		state = !state;
		buttonHandler.handleElementButtonClick(this, this.getName(), lastMouseButton);
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
}
