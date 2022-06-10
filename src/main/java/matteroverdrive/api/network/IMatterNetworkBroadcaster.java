
package matteroverdrive.api.network;

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 7/17/2015. This is used by machines that can broadcast
 * messaged over the Matter Network.
 */
public interface IMatterNetworkBroadcaster extends IMatterNetworkConnection {
	/**
	 * Gets the filter used in the broadcasted packets.
	 *
	 * @return the broadcast filter. This usually contains a list of Block inates
	 *         the broadcaster can broadcast to.
	 */
	NBTTagCompound getFilter();
}
