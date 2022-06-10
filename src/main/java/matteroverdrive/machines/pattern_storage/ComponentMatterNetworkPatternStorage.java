
package matteroverdrive.machines.pattern_storage;

import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;

public class ComponentMatterNetworkPatternStorage
		extends MatterNetworkComponentClient<TileEntityMachinePatternStorage> {
	public ComponentMatterNetworkPatternStorage(TileEntityMachinePatternStorage patternStorage) {
		super(patternStorage);
	}

	@Override
	public void onNetworkEvent(IMatterNetworkEvent event) {
		if (event instanceof IMatterNetworkEvent.Task
				&& ((IMatterNetworkEvent.Task) event).task instanceof MatterNetworkTaskStorePattern) {
			onTask((MatterNetworkTaskStorePattern) ((IMatterNetworkEvent.Task) event).task);
		}
	}

	private void onTask(MatterNetworkTaskStorePattern task) {
		if (task.getState().belowOrEqual(MatterNetworkTaskState.WAITING)
				&& rootClient.addItem(task.getItemStack(), task.getProgress(), false, null)) {
			task.setState(MatterNetworkTaskState.FINISHED);
		}
	}
}
