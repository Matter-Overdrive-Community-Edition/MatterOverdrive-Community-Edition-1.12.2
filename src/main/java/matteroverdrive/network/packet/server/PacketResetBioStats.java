
package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketResetBioStats extends PacketAbstract {
	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketResetBioStats> {

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketResetBioStats message, MessageContext ctx) {
			AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
			if (androidPlayer != null && androidPlayer.isAndroid()) {
				player.addExperienceLevel(androidPlayer.resetUnlocked());
			}
		}
	}
}
