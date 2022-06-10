
package matteroverdrive.gui.element;

import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;

public class ElementStatesHoloIcons extends MOElementButtonScaled {
	HoloIcon[] states;
	int selectedState;

	public ElementStatesHoloIcons(MOGuiBase gui, IButtonHandler buttonHandler, int posX, int posY, int sizeX, int sizeY,
			String name, HoloIcon[] states) {
		super(gui, buttonHandler, posX, posY, name, sizeX, sizeY);
		this.states = states;
	}

	public HoloIcon[] getStates() {
		return states;
	}

	public void setStates(HoloIcon[] states) {
		this.states = states;
	}

	public int getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(int selectedState) {
		this.selectedState = selectedState;
		this.icon = states[selectedState];
	}

	@Override
	public void onAction(int mouseX, int mouseY, int mouseButton) {
		selectedState++;
		if (selectedState >= states.length) {
			selectedState = 0;
		}

		if (selectedState < states.length) {
			icon = states[selectedState];
		}
		buttonHandler.handleElementButtonClick(this, name, selectedState);
	}
}
