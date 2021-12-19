
package matteroverdrive.handler.matter_network;

import matteroverdrive.data.transport.FluidPipeNetwork;
import matteroverdrive.data.transport.IFluidPipe;

public class FluidNetworkHandler extends GridNetworkHandler<IFluidPipe, FluidPipeNetwork> {
    @Override
    public FluidPipeNetwork createNewNetwork(IFluidPipe node) {
        return new FluidPipeNetwork(this);
    }
}
