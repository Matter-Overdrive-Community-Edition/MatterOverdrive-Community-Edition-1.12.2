
package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadEnergyWeapon extends PacketAbstract {
	public PacketReloadEnergyWeapon() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketReloadEnergyWeapon> {
		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketReloadEnergyWeapon message, MessageContext ctx) {
			if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()
					&& player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof EnergyWeapon) {
				if (((EnergyWeapon) player.getHeldItem(EnumHand.MAIN_HAND).getItem())
						.needsRecharge(player.getHeldItem(EnumHand.MAIN_HAND))) {
					((EnergyWeapon) player.getHeldItem(EnumHand.MAIN_HAND).getItem())
							.chargeFromEnergyPack(player.getHeldItem(EnumHand.MAIN_HAND), player);
				}
			}
		}
	}
}