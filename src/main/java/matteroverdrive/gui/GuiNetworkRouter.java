
package matteroverdrive.gui;

import matteroverdrive.container.ContainerFactory;
import matteroverdrive.gui.element.ElementConnections;
import matteroverdrive.tile.TileEntityMachineNetworkRouter;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiNetworkRouter extends MOGuiMachine<TileEntityMachineNetworkRouter> {
    ElementConnections connections;

    public GuiNetworkRouter(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkRouter entity) {
        super(ContainerFactory.createMachineContainer(entity, inventoryPlayer), entity);
        name = "network_router";
        connections = new ElementConnections(this, 50, 42, xSize - 74, ySize, machine);
    }

    @Override
    public void initGui() {
        super.initGui();
        pages.get(0).addElement(connections);
        AddHotbarPlayerSlots(inventorySlots, this);
    }

}
