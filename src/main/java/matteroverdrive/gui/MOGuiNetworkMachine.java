
package matteroverdrive.gui;

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.pages.MatterNetworkConfigPage;
import matteroverdrive.machines.MOTileEntityMachine;

public abstract class MOGuiNetworkMachine<T extends MOTileEntityMachine & IMatterNetworkConnection>
		extends MOGuiMachine<T> {

	public MOGuiNetworkMachine(ContainerMachine<T> container, T machine) {
		super(container, machine);
	}

	public MOGuiNetworkMachine(ContainerMachine<T> container, T machine, int width, int height) {
		super(container, machine, width, height);
	}

	public void registerPages(MOBaseContainer container, T machine) {
		super.registerPages(container, machine);
		MatterNetworkConfigPage configPage = new MatterNetworkConfigPage(this, 48, 32, xSize - 76, ySize);
		configPage.setName("Configurations");

		pages.set(1, configPage);
	}
}
