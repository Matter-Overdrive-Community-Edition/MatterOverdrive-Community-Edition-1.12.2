
package matteroverdrive.handler.matter_network;

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.data.transport.MatterNetwork;

public class MatterNetworkHandler extends GridNetworkHandler<IMatterNetworkConnection, MatterNetwork> {
	@Override
	public MatterNetwork createNewNetwork(IMatterNetworkConnection node) {
		return new MatterNetwork(this);
	}
}
