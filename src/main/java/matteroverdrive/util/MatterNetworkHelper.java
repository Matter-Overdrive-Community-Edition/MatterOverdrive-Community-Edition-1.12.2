
package matteroverdrive.util;

import matteroverdrive.api.network.IMatterNetworkFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class MatterNetworkHelper {
	public static NBTTagCompound getFilterFromPositions(BlockPos... positions) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for (BlockPos position : positions) {
			tagList.appendTag(new NBTTagLong(position.toLong()));
		}
		tagCompound.setTag(IMatterNetworkFilter.CONNECTIONS_TAG, tagList);
		return tagCompound;
	}

	public static NBTTagCompound addPositionsToFilter(NBTTagCompound filter, BlockPos... positions) {
		if (filter == null) {
			filter = new NBTTagCompound();
		}

		NBTTagList tagList = filter.getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_COMPOUND);
		for (BlockPos position : positions) {
			tagList.appendTag(new NBTTagLong(position.toLong()));
		}
		filter.setTag(IMatterNetworkFilter.CONNECTIONS_TAG, tagList);
		return filter;
	}
}
