
package matteroverdrive.api.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 7/22/2015. This is used by machines that have a Matter
 * Network filter. This is mainly used in conjunction with
 * {@link IMatterNetworkBroadcaster} or {@link IMatterNetworkDispatcher} to
 * filter the destinations.
 */
public interface IMatterNetworkFilter {
	/**
	 * The NBT Tag name of the connections list.
	 */
	String CONNECTIONS_TAG = "CONNECTIONS";

	/**
	 * Gets the filter for a given stack filter. This is used to get the filter from
	 * the item in the item filter slot of the machine.
	 *
	 * @param stack the filter stack
	 * @return the NBT of the filter.
	 * @see matteroverdrive.items.NetworkFlashDrive
	 */
	NBTTagCompound getFilter(ItemStack stack);
}
