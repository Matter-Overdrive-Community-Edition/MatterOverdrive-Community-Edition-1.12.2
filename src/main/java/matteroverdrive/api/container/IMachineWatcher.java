
package matteroverdrive.api.container;

import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public interface IMachineWatcher {
	EntityPlayer getPlayer();

	void onWatcherAdded(MOTileEntityMachine machine);

	boolean isWatcherValid();
}
