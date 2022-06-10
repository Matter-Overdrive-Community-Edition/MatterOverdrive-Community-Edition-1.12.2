
package matteroverdrive.container.matter_network;

import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.data.matter_network.MatterDatabaseEvent;

public interface IMatterDatabaseWatcher extends IMachineWatcher {
	void onConnectToNetwork(IMatterDatabaseMonitor monitor);

	void onDisconnectFromNetwork(IMatterDatabaseMonitor monitor);

	void onDatabaseEvent(MatterDatabaseEvent changeInfo);
}
