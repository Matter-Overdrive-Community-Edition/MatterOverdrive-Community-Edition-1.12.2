
package matteroverdrive.api.transport;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by Simeon on 5/5/2015. Stores inates and the name of transport
 * locations. Used by the transporter.
 */
public class TransportLocation {
	/**
	 * The X,Y,Z inates of the location.
	 */
	public BlockPos pos;
	/**
	 * The name of the location.
	 */
	public String name;

	public TransportLocation(BlockPos pos, String name) {
		this.pos = pos;
		this.name = name;
	}

	public TransportLocation(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	public TransportLocation(NBTTagCompound nbt) {
		if (nbt != null) {
			pos = BlockPos.fromLong(nbt.getLong("tl"));
			name = nbt.getString("tl_name");
		}
	}

	public void writeToBuffer(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setLong("tl", pos.toLong());
		nbtTagCompound.setString("tl_name", name);
	}

	/**
	 * Sets the name of the transport location.
	 *
	 * @param name the new transport location name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the transport location inates.
	 */
	public void setPosition(BlockPos pos) {
		this.pos = pos;
	}

	/**
	 * Calculates and returns the distance between this location and the given
	 * inates.
	 *
	 * @param pos the given position.
	 * @return the distance between this transport location and the provided inates.
	 */
	public int getDistance(BlockPos pos) {
		return (int) Math.sqrt(pos.distanceSq(this.pos));
	}
}
