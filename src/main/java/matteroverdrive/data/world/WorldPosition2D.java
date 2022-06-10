
package matteroverdrive.data.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.io.Serializable;

public class WorldPosition2D implements Serializable {

	public int x, z;

	public WorldPosition2D(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public WorldPosition2D(NBTTagCompound tagCompound) {
		readFromNBT(tagCompound);
	}

	public WorldPosition2D(ByteBuf byteBuf) {
		readFromBuffer(byteBuf);
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("wp_x", x);
		tagCompound.setInteger("wp_z", z);
	}

	public void writeToBuffer(ByteBuf byteBuf) {
		byteBuf.writeInt(x);
		byteBuf.writeInt(z);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		x = tagCompound.getInteger("wp_x");
		z = tagCompound.getInteger("wp_z");
	}

	public void readFromBuffer(ByteBuf byteBuf) {
		x = byteBuf.readInt();
		z = byteBuf.readInt();
	}

	public int manhattanDistance(WorldPosition2D other) {
		return manhattanDistance(other.x, other.z);
	}

	public int manhattanDistance(int x, int z) {
		return Math.abs(this.x - x) + Math.abs(this.z - z);
	}

	public int hashCode() {
		return this.x & 4095 | this.z & '\uff00';
	}
}
