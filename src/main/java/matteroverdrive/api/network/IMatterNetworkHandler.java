
package matteroverdrive.api.network;

import matteroverdrive.tile.IMOTickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Simeon on 7/13/2015. This is used by all Matter Network Machines.
 * This is just for calling the custom tick method for all Matter Network
 * Machines.
 */
public interface IMatterNetworkHandler extends IMOTickable {
	/**
	 * Called each matter Network tick. This is handled separately from the Tile
	 * Entity's update function. It has to phases. A start and an end. The Matter
	 * Network ticking is limited based on the number of interaction on the Network
	 * for each Minecraft world tick.
	 *
	 * @param world the world.
	 * @param phase the phase of the Matter Network tick. There are 2 phases:
	 *              <ul>
	 *              <li>Start</li>
	 *              <li>End</li>
	 *              </ul>
	 * @return the number of interaction made this tick. Used to limit the
	 *         interaction per World tick.
	 */
	int onNetworkTick(World world, TickEvent.Phase phase);

	default void onServerTick(TickEvent.Phase phase, World world) {
		onNetworkTick(world, phase);
	}
}
