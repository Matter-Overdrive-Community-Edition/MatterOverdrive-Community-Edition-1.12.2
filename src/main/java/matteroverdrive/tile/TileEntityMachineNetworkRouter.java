
package matteroverdrive.tile;

import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.util.EnumFacing;

public class TileEntityMachineNetworkRouter extends TileEntityMachinePacketQueue {

    public TileEntityMachineNetworkRouter() {
        super(4);
        playerSlotsHotbar = true;
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }
}
