
package matteroverdrive.network.packet;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractBiPacketHandler<T extends IMessage> extends AbstractPacketHandler<T> {
	public AbstractBiPacketHandler() {
	}
}
