
package matteroverdrive.machines.pattern_monitor;

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.container.matter_network.IMatterDatabaseWatcher;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.data.matter_network.MatterDatabaseEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;

public class ComponentMatterNetworkPatternMonitor
		extends MatterNetworkComponentClient<TileEntityMachinePatternMonitor> {
	public ComponentMatterNetworkPatternMonitor(TileEntityMachinePatternMonitor patternMonitor) {
		super(patternMonitor);
	}

	@Override
	public void onNetworkEvent(IMatterNetworkEvent event) {
		if (event instanceof IMatterNetworkEvent.ClientAdded) {
			onClientAdded((IMatterNetworkEvent.ClientAdded) event);
		} else if (event instanceof IMatterNetworkEvent.AddedToNetwork) {
			onAddedToNetwork((IMatterNetworkEvent.AddedToNetwork) event);
		} else if (event instanceof IMatterNetworkEvent.RemovedFromNetwork) {
			onRemovedFromNetwork((IMatterNetworkEvent.RemovedFromNetwork) event);
		} else if (event instanceof IMatterNetworkEvent.ClientRemoved) {
			onClientRemoved((IMatterNetworkEvent.ClientRemoved) event);
		} else if (event instanceof MatterDatabaseEvent) {
			onPatternChange((MatterDatabaseEvent) event);
		}
	}

	private void onRemovedFromNetwork(IMatterNetworkEvent.RemovedFromNetwork event) {
		rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher)
				.forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDisconnectFromNetwork(rootClient));
	}

	private void onAddedToNetwork(IMatterNetworkEvent.AddedToNetwork event) {
		rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher)
				.forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onConnectToNetwork(rootClient));
	}

	private void onClientAdded(IMatterNetworkEvent.ClientAdded event) {
		if (event.client instanceof IMatterDatabase) {
			MatterDatabaseEvent databaseEvent = new MatterDatabaseEvent.Added((IMatterDatabase) event.client);
			rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher)
					.forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(databaseEvent));
		}
	}

	private void onClientRemoved(IMatterNetworkEvent.ClientRemoved event) {
		if (event.client instanceof IMatterDatabase) {
			MatterDatabaseEvent databaseEvent = new MatterDatabaseEvent.Removed((IMatterDatabase) event.client);
			rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher)
					.forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(databaseEvent));
		}
	}

	private void onPatternChange(MatterDatabaseEvent event) {
		rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher)
				.forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(event));
	}
}
